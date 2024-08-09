package handlers

import (
	"backend/internal/models"
	"backend/internal/services"
	"errors"
	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
	"net/http"
	"strconv"
)

// TODO: create a specific update password function for creation and update

func CreateUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		var user models.User
		if err := c.ShouldBindJSON(&user); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input data"})
			return
		}

		err := services.NewUserService(db).CreateUser(&user)
		if errors.Is(err, services.ErrInvalidUserData) {
			c.JSON(http.StatusBadRequest, gin.H{"error": services.ErrInvalidUserData.Error()})
			return
		}
		if errors.Is(err, services.ErrUserAlreadyExists) {
			c.JSON(http.StatusConflict, gin.H{"error": services.ErrUserAlreadyExists.Error()})
			return
		}

		c.JSON(http.StatusCreated, user.ToResponse())
	}
}

func GetUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		id := c.Param("id")
		userID, err := strconv.Atoi(id)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid user ID"})
			return
		}

		user, err := services.NewUserService(db).GetUserByID(uint(userID))
		if errors.Is(err, services.ErrUserNotFound) {
			c.JSON(http.StatusNotFound, gin.H{"error": services.ErrUserNotFound.Error()})
			return
		} else if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, user.ToResponse())
	}
}

func UpdateUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		id := c.Param("id")
		userID, err := strconv.Atoi(id)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid user ID"})
			return
		}

		var updatedUser models.User
		if err := c.ShouldBindJSON(&updatedUser); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input data"})
			return
		}

		err = services.NewUserService(db).UpdateUser(uint(userID), &updatedUser)
		if errors.Is(err, services.ErrUserNotFound) {
			c.JSON(http.StatusNotFound, gin.H{"error": services.ErrUserNotFound.Error()})
			return
		} else if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Error updating user"})
			return
		}

		c.JSON(http.StatusOK, updatedUser.ToResponse())
	}
}

func DeleteUser(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		id := c.Param("id")
		userID, err := strconv.Atoi(id)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid user ID"})
			return
		}

		err = services.NewUserService(db).DeleteUser(uint(userID))
		if errors.Is(err, services.ErrUserNotFound) {
			c.JSON(http.StatusNotFound, gin.H{"error": services.ErrUserNotFound.Error()})
			return
		} else if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Error updating user"})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": "User deleted successfully"})
	}
}
