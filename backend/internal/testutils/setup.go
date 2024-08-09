package testutils

import (
	"backend/internal/models"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"strconv"
)

// SetupTestDB initializes an in-memory SQLite database and migrates models.
func SetupTestDB() *gorm.DB {
	db, err := gorm.Open(sqlite.Open("file::memory:"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&models.User{}, &models.Collection{}, &models.Card{}) // Migrate models as needed

	return db
}

func UintToString(id uint) string {
	return strconv.FormatUint(uint64(id), 10)
}
