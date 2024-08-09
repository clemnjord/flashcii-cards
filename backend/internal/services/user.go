package services

import (
	"backend/internal/models"
	"errors"
	"gorm.io/gorm"
)

// Define errors for specific cases
var (
	ErrUserAlreadyExists = errors.New("user with this email already exists")
	ErrInvalidUserData   = errors.New("invalid user data")
	ErrUserNotFound      = errors.New("user not found")
)

type UserService struct {
	db *gorm.DB
}

// NewUserService creates a new instance of UserService
func NewUserService(db *gorm.DB) *UserService {
	return &UserService{db: db}
}

// CreateUser creates a new user in the database
func (s *UserService) CreateUser(user *models.User) error {
	// Validate input data
	if user.Name == "" || user.Email == "" || user.Password == "" {
		return ErrInvalidUserData
	}

	// Check if user with the given email already exists
	var existingUser models.User
	if err := s.db.Where("email = ?", user.Email).First(&existingUser).Error; err == nil {
		return ErrUserAlreadyExists
	} else if !errors.Is(err, gorm.ErrRecordNotFound) {
		return err
	}

	// Create the new user
	if err := s.db.Create(user).Error; err != nil {
		return err
	}

	return nil
}

func (s *UserService) GetUserByID(id uint) (*models.User, error) {
	var user models.User
	if err := s.db.First(&user, id).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, ErrUserNotFound
		}
		return nil, err
	}
	return &user, nil
}

func (s *UserService) UpdateUser(userID uint, updatedUser *models.User) error {
	user, err := s.GetUserByID(userID)
	if err != nil {
		return err
	}

	// Update the user's fields
	if updatedUser.Name != "" {
		user.Name = updatedUser.Name
	}
	if updatedUser.Email != "" {
		user.Email = updatedUser.Email
	}
	if updatedUser.Password != "" {
		user.Password = updatedUser.Password
	}

	// TODO: Validate email not used
	// Save the changes to the database
	if err := s.db.Save(&user).Error; err != nil {
		return err
	}

	return nil
}

func (s *UserService) DeleteUser(userID uint) error {
	user, err := s.GetUserByID(userID)
	if err != nil {
		return err
	}

	// Delete the user from the database
	if err := s.db.Delete(&user).Error; err != nil {
		return err
	}

	return nil
}
