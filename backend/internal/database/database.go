package database

import (
	"backend/internal/config"
	"backend/internal/models"
	"errors"
	"github.com/open-spaced-repetition/go-fsrs"
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
	db.AutoMigrate(&models.User{})
	db.AutoMigrate(&models.Collection{})
	db.AutoMigrate(&models.Card{})

	// Create default user

	user := &models.User{Name: "admin", Password: "admin"}
	db.FirstOrCreate(user, user)

	// create test collection
	collection := &models.Collection{UserID: user.ID, Title: "Test Collection", CollectionUrl: "www.test.com", UserName: "adminA", Password: "adminA", CollectionOwnerThemes: "testA", CollectionUserThemes: "testB", CollectionVersion: "1.0.0"}
	db.FirstOrCreate(collection, collection)

	// Create test cards
	card := &models.Card{
		Model: gorm.Model{
			ID: 1,
		},
		CollectionID: collection.ID,
		Title:        "Card 1",
		DataPath:     "1",
		FSRSCard:     fsrs.NewCard(),
	}

	db.FirstOrCreate(card)

	card = &models.Card{
		Model: gorm.Model{
			ID: 2,
		},
		CollectionID: collection.ID,
		Title:        "Card 2",
		DataPath:     "2",
		FSRSCard:     fsrs.NewCard(),
	}
	db.FirstOrCreate(card)

	return db
}
