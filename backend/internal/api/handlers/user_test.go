package handlers

import (
	"backend/internal/models"
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"strconv"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

func setupTestDB() *gorm.DB {
	db, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&models.User{})

	return db
}

func setupRouter(db *gorm.DB) *gin.Engine {
	r := gin.Default()
	r.POST("/users", CreateUser(db))
	r.GET("/users/:id", GetUser(db))
	r.PUT("/users/:id", UpdateUser(db))
	r.DELETE("/users/:id", DeleteUser(db))
	return r
}

func createRequest(method, url string, body interface{}) (*http.Request, *httptest.ResponseRecorder) {
	jsonData, err := json.Marshal(body)
	if err != nil {
		panic(err)
	}
	req, err := http.NewRequest(method, url, bytes.NewBuffer(jsonData))
	if err != nil {
		panic(err)
	}
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	return req, w
}

func uintToString(id uint) string {
	return strconv.FormatUint(uint64(id), 10)
}

// Test cases
func TestCreateUser_Success(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}

	req, w := createRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusCreated, w.Code)
	var responseUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)
	assert.Equal(t, userData["name"], responseUser.Name)
	assert.Equal(t, userData["email"], responseUser.Email)
	assert.NotZero(t, responseUser.ID)
}

func TestCreateUser_MissingField(t *testing.T) {
	tests := []struct {
		name     string
		userData map[string]string
		expected string
	}{
		{"Name", map[string]string{"email": "test@test.com", "password": "password"}, "Name is required"},
		{"Password", map[string]string{"name": "John Doe", "email": "test@test.com"}, "Password is required"},
		{"Email", map[string]string{"name": "John Doe", "password": "password"}, "Email is required"},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			db := setupTestDB()
			r := setupRouter(db)

			req, w := createRequest(http.MethodPost, "/users", tt.userData)
			r.ServeHTTP(w, req)

			assert.Equal(t, http.StatusBadRequest, w.Code)
			var response map[string]string
			err := json.Unmarshal(w.Body.Bytes(), &response)
			assert.NoError(t, err)
			assert.Contains(t, response["error"], tt.expected)
		})
	}
}

func TestCreateUser_BadInput(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	req, err := http.NewRequest(http.MethodPost, "/users", bytes.NewBuffer([]byte("bad input")))
	assert.NoError(t, err)
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusBadRequest, w.Code)
	var response map[string]string
	err = json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid input data")
}

func TestGetUser_Success(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}

	req, w := createRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusCreated, w.Code)

	var createdUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &createdUser)
	assert.NoError(t, err)
	assert.NotZero(t, createdUser.ID)

	req, w = createRequest(http.MethodGet, "/users/"+uintToString(createdUser.ID), nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	var fetchedUser models.UserResponse
	err = json.Unmarshal(w.Body.Bytes(), &fetchedUser)
	assert.NoError(t, err)
	assert.Equal(t, fetchedUser.ID, createdUser.ID)
	assert.Equal(t, fetchedUser.Name, createdUser.Name)
	assert.Equal(t, fetchedUser.Email, createdUser.Email)
}

func TestGetUser_NoUser(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	req, w := createRequest(http.MethodGet, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "User not found")
}

func TestGetUser_BadRequest(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	req, w := createRequest(http.MethodGet, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestUpdateUser_Success(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}
	req, w := createRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusCreated, w.Code)

	var createdUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &createdUser)
	assert.NoError(t, err)
	assert.NotZero(t, createdUser.ID)

	updatedData := map[string]string{
		"name":     "Jane Doe",
		"password": "newpassword123",
		"email":    "jane@example.com",
	}
	req, w = createRequest(http.MethodPut, "/users/"+uintToString(createdUser.ID), updatedData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	var responseUser models.UserResponse
	err = json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)
	assert.Equal(t, "Jane Doe", responseUser.Name)
	assert.Equal(t, "jane@example.com", responseUser.Email)
}

func TestUpdateUser_NoUser(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Update a user that doesn't exist
	req, w := createRequest(http.MethodPut, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "User not found")
}

func TestUpdateUser_BadRequest(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Update a user with a non-integer ID
	req, w := createRequest(http.MethodPut, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestUpdateUser_BadInput(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Create a user
	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}
	req, w := createRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusCreated, w.Code)

	var createdUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &createdUser)
	assert.NoError(t, err)
	assert.NotZero(t, createdUser.ID)

	// Update the user with bad input
	req, w = createRequest(http.MethodPut, "/users/"+uintToString(createdUser.ID), "bad input")
	assert.NoError(t, err)
	req.Header.Set("Content-Type", "application/json")
	w = httptest.NewRecorder()
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusBadRequest, w.Code)
	var response map[string]string
	err = json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid input data")
}

func TestDeleteUser_Success(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Create a user
	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}

	// Create a user
	req, w := createRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusCreated, w.Code)
	var responseUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)

	// Delete a user
	req, w = createRequest(http.MethodDelete, "/users/"+uintToString(responseUser.ID), nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	// Try to fetch deleted user
	req, w = createRequest(http.MethodGet, "/users/"+uintToString(responseUser.ID), nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)
}

// TODO: Once collection CRUD and card CRUD have been implemented,
//       add a test checking that cascading deletion works properly: TestDeleteUser_CascadingDeletion

func TestDeleteUser_BadRequest(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Update a user with a non-integer ID
	req, w := createRequest(http.MethodDelete, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestDeleteUser_NoUser(t *testing.T) {
	db := setupTestDB()
	r := setupRouter(db)

	// Update a user that doesn't exist
	req, w := createRequest(http.MethodDelete, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "User not found")
}
