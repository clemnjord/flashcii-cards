package database

import (
	"backend/internal/config"
	"backend/internal/model"
	"errors"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"os"
)

func DatabaseConnection() *gorm.DB {

	var options = config.GetInstance()
	var databaseName = "test.db"
	var databaseInstance = options.DatabasePath() + "/" + databaseName

	// check if database exists
	if _, err := os.Stat(databaseInstance); errors.Is(err, os.ErrNotExist) {
		// path/to/whatever does not exist
		os.Create(databaseInstance)
	}

	db, err := gorm.Open(sqlite.Open(options.DatabasePath()+"/test.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	// Migrate the schema
	db.AutoMigrate(&model.User{})
	db.AutoMigrate(&model.Card{})

	// Create default user
	var user model.User
	tx := db.First(&user, 1)
	if tx.Error != nil {
		db.Create(&model.User{Name: "admin", Password: "admin"})
	}

	//Create test cards
	card := &model.Card{Title: "Card 1", DataPath: "test1"}
	db.FirstOrCreate(card, card)
	card = &model.Card{Title: "Card 2", DataPath: "test2"}
	db.FirstOrCreate(card, card)

	return db
}
