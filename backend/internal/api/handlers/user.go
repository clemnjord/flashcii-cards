package handlers

import (
	"backend/internal/models"
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

func getUserByID(db *gorm.DB, id string) (*models.User, int, string) {
	userID, err := strconv.Atoi(id)
	if err != nil {
		return nil, http.StatusBadRequest, "Invalid user ID"
	}

	var user models.User
	if err := db.First(&user, userID).Error; err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, http.StatusNotFound, "User not found"
		}
		return nil, http.StatusInternalServerError, "An unexpected error occurred"
	}

	return &user, http.StatusOK, ""
}

func CreateUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		var user models.User
		if err := c.ShouldBindJSON(&user); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input data"})
			return
		}

		if &user.Name == nil || user.Name == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Name is required"})
			return
		}

		if &user.Email == nil || user.Email == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Email is required"})
			return
		}

		if &user.Password == nil || user.Password == "" {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Password is required"})
			return
		}

		if err := db.Create(&user).Error; err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusCreated, user.ToResponse())
	}
}

func GetUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		user, status, errMessage := getUserByID(db, c.Param("id"))
		if status != http.StatusOK {
			c.JSON(status, gin.H{"error": errMessage})
			return
		}

		c.JSON(http.StatusOK, user.ToResponse())
	}
}

// TODO: create a specific update password function ?
func UpdateUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		user, status, errMessage := getUserByID(db, c.Param("id"))
		if status != http.StatusOK {
			c.JSON(status, gin.H{"error": errMessage})
			return
		}

		var newUser models.User
		if err := c.ShouldBindJSON(&newUser); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input data"})
			return
		}

		updates := map[string]interface{}{}
		if newUser.Name != "" {
			updates["name"] = newUser.Name
		}
		if newUser.Email != "" {
			updates["email"] = newUser.Email
		}

		if len(updates) > 0 {
			if err := db.Model(&user).Updates(updates).Error; err != nil {
				c.JSON(http.StatusInternalServerError, gin.H{"error": "Error updating user"})
				return
			}
		}

		c.JSON(http.StatusOK, user.ToResponse())
	}
}

func DeleteUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		user, status, errMessage := getUserByID(db, c.Param("id"))
		if status != http.StatusOK {
			c.JSON(status, gin.H{"error": errMessage})
			return
		}

		// Delete the user
		if err := db.Delete(&user).Error; err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": "User deleted successfully"})
	}
}
