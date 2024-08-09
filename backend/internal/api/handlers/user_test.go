package handlers

import (
	"backend/internal/models"
	"backend/internal/services"
	"backend/internal/testutils"
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
	"gorm.io/gorm"
)

// The goal of the following tests is to check that the return statuses and DTOs are correct.

func setupRouterUser(db *gorm.DB) *gin.Engine {
	r := gin.Default()
	r.POST("/users", CreateUser(db))
	r.GET("/users/:id", GetUser(db))
	r.PUT("/users/:id", UpdateUser(db))
	r.DELETE("/users/:id", DeleteUser(db))
	return r
}

// Test cases
func TestCreateUser_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}

	req, w := testutils.CreateRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusCreated, w.Code)
	var responseUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)
	assert.Equal(t, userData["name"], responseUser.Name)
	assert.Equal(t, userData["email"], responseUser.Email)
	assert.NotZero(t, responseUser.ID)
}

// Test cases
func TestCreateUser_UserAlreadyExists(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	userData := map[string]string{
		"name":     "John Doe",
		"password": "password123",
		"email":    "john@example.com",
	}

	req, w := testutils.CreateRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusCreated, w.Code)
	var responseUser models.UserResponse
	err := json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)
	assert.Equal(t, userData["name"], responseUser.Name)
	assert.Equal(t, userData["email"], responseUser.Email)
	assert.NotZero(t, responseUser.ID)

	// Create existing user
	req, w = testutils.CreateRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusConflict, w.Code)
	var response map[string]string
	err = json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], services.ErrUserAlreadyExists.Error())
}

func TestCreateUser_MissingField(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	userData := map[string]string{
		"password": "password123",
		"email":    "john@example.com",
	}

	req, w := testutils.CreateRequest(http.MethodPost, "/users", userData)
	r.ServeHTTP(w, req)

	assert.Equal(t, http.StatusBadRequest, w.Code)
	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], services.ErrInvalidUserData.Error())
}

func TestCreateUser_BadInput(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

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
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	user := models.User{
		Name:     "John Doe",
		Password: "password123",
		Email:    "john@example.com",
	}

	err := services.NewUserService(db).CreateUser(&user)
	assert.NoError(t, err)

	req, w := testutils.CreateRequest(http.MethodGet, "/users/"+testutils.UintToString(user.ID), nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	var fetchedUser models.UserResponse
	err = json.Unmarshal(w.Body.Bytes(), &fetchedUser)
	assert.NoError(t, err)
	assert.Equal(t, fetchedUser.ID, user.ID)
	assert.Equal(t, fetchedUser.Name, user.Name)
	assert.Equal(t, fetchedUser.Email, user.Email)
}

func TestGetUser_NoUser(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	req, w := testutils.CreateRequest(http.MethodGet, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], services.ErrUserNotFound.Error())
}

func TestGetUser_BadRequest(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	req, w := testutils.CreateRequest(http.MethodGet, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestUpdateUser_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	user := models.User{
		Name:     "John Doe",
		Password: "password123",
		Email:    "john@example.com",
	}

	err := services.NewUserService(db).CreateUser(&user)
	assert.NoError(t, err)

	updatedData := map[string]string{
		"name":     "Jane Doe",
		"password": "newpassword123",
		"email":    "jane@example.com",
	}
	req, w := testutils.CreateRequest(http.MethodPut, "/users/"+testutils.UintToString(user.ID), updatedData)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	var responseUser models.UserResponse
	err = json.Unmarshal(w.Body.Bytes(), &responseUser)
	assert.NoError(t, err)
	assert.Equal(t, "Jane Doe", responseUser.Name)
	assert.Equal(t, "jane@example.com", responseUser.Email)
}

func TestUpdateUser_NoUser(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	// Update a user that doesn't exist
	req, w := testutils.CreateRequest(http.MethodPut, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], services.ErrUserNotFound.Error())
}

func TestUpdateUser_BadRequest(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	// Update a user with a non-integer ID
	req, w := testutils.CreateRequest(http.MethodPut, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestUpdateUser_BadInput(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	user := models.User{
		Name:     "John Doe",
		Password: "password123",
		Email:    "john@example.com",
	}

	err := services.NewUserService(db).CreateUser(&user)
	assert.NoError(t, err)

	// Update the user with bad input
	req, w := testutils.CreateRequest(http.MethodPut, "/users/"+testutils.UintToString(user.ID), "bad input")
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
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	user := models.User{
		Name:     "John Doe",
		Password: "password123",
		Email:    "john@example.com",
	}

	err := services.NewUserService(db).CreateUser(&user)
	assert.NoError(t, err)

	// Delete a user
	req, w := testutils.CreateRequest(http.MethodDelete, "/users/"+testutils.UintToString(user.ID), nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusOK, w.Code)

	_, err = services.NewUserService(db).GetUserByID(user.ID)
	assert.Error(t, services.ErrUserNotFound, err)
}

// TODO: Once collection CRUD and card CRUD have been implemented,
//       add a test checking that cascading deletion works properly: TestDeleteUser_CascadingDeletion

func TestDeleteUser_BadRequest(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	// Update a user with a non-integer ID
	req, w := testutils.CreateRequest(http.MethodDelete, "/users/routeError", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusBadRequest, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], "Invalid user ID")
}

func TestDeleteUser_NoUser(t *testing.T) {
	db := testutils.SetupTestDB()
	r := setupRouterUser(db)

	// Update a user that doesn't exist
	req, w := testutils.CreateRequest(http.MethodDelete, "/users/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, http.StatusNotFound, w.Code)

	var response map[string]string
	err := json.Unmarshal(w.Body.Bytes(), &response)
	assert.NoError(t, err)
	assert.Contains(t, response["error"], services.ErrUserNotFound.Error())
}
