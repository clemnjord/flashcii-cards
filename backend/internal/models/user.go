package models

import (
	"gorm.io/gorm"
)

type User struct {
	gorm.Model
	Name     string `json:"name"`
	Password string `json:"password"`
	Email    string `json:"email"`
}

type UserResponse struct {
	ID    uint   `json:"id"`
	Name  string `json:"name"`
	Email string `json:"email"`
}

// ToResponse Convert User model to UserResponse DTO
func (u *User) ToResponse() UserResponse {
	return UserResponse{
		ID:    u.ID,
		Name:  u.Name,
		Email: u.Email,
	}
}
