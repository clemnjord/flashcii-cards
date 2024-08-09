package services

import (
	"backend/internal/models"
	"backend/internal/testutils"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestCreateUser_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	user := &models.User{
		Name:     "John Doe",
		Email:    "john@example.com",
		Password: "password123",
	}

	err := userService.CreateUser(user)
	assert.NoError(t, err)
	assert.NotZero(t, user.ID)
}

func TestCreateUser_UserAlreadyExists(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	user := &models.User{
		Name:     "John Doe",
		Email:    "john@example.com",
		Password: "password123",
	}

	err := userService.CreateUser(user)
	assert.NoError(t, err)

	// Try creating the same user again
	err = userService.CreateUser(user)
	assert.Error(t, err)
	assert.Equal(t, ErrUserAlreadyExists, err)
}

func TestCreateUser_MissingField(t *testing.T) {
	tests := []struct {
		name     string
		user     models.User
		expected string
	}{
		{"Name", models.User{Email: "test@test.com", Password: "password"}, ErrInvalidUserData.Error()},
		{"Password", models.User{Name: "John Doe", Email: "test@test.com"}, ErrInvalidUserData.Error()},
		{"Email", models.User{Name: "John Doe", Password: "password"}, ErrInvalidUserData.Error()},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			db := testutils.SetupTestDB()
			userService := NewUserService(db)

			err := userService.CreateUser(&tt.user)
			assert.EqualError(t, err, tt.expected)
		})
	}
}

func TestGetUserByID_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	user := &models.User{
		Name:     "John Doe",
		Email:    "john@example.com",
		Password: "password123",
	}

	err := userService.CreateUser(user)

	fetchedUser, err := userService.GetUserByID(user.ID)
	assert.NoError(t, err)
	assert.Equal(t, fetchedUser.Name, user.Name)
	assert.Equal(t, fetchedUser.Email, user.Email)
	assert.Equal(t, fetchedUser.ID, user.ID)
}

func TestGetUserByID_NoUser(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	_, err := userService.GetUserByID(1)
	assert.Error(t, err)
	assert.Equal(t, ErrUserNotFound, err)
}

func TestUpdateUser_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	user := &models.User{
		Name:     "John Doe",
		Email:    "john@example.com",
		Password: "password123",
	}
	err := userService.CreateUser(user)
	assert.NoError(t, err)

	updatedUser := models.User{
		Name:     "Jane Doe",
		Email:    "jane@example.com",
		Password: "newpassword123",
	}
	err = userService.UpdateUser(user.ID, &updatedUser)
	assert.NoError(t, err)

	fetchedUser, err := userService.GetUserByID(user.ID)
	assert.NoError(t, err)
	assert.Equal(t, "Jane Doe", fetchedUser.Name)
	assert.Equal(t, "jane@example.com", fetchedUser.Email)
}

func TestDeleteUser_Success(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	user := &models.User{
		Name:     "John Doe",
		Email:    "john@example.com",
		Password: "password123",
	}
	err := userService.CreateUser(user)
	assert.NoError(t, err)

	err = userService.DeleteUser(user.ID)
	assert.NoError(t, err)

	_, err = userService.GetUserByID(user.ID)
	assert.Error(t, err, ErrUserNotFound)
}

func TestDeleteUser_NoUser(t *testing.T) {
	db := testutils.SetupTestDB()
	userService := NewUserService(db)

	err := userService.DeleteUser(1)
	assert.Equal(t, err, ErrUserNotFound)
}
