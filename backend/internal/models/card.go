package models

import (
	"github.com/open-spaced-repetition/go-fsrs"
	"gorm.io/gorm"
	"time"
)

type Card struct {
	gorm.Model
	CollectionID uint `gorm:"constraint:OnDelete:CASCADE;"`
	Title        string
	DataPath     string
	LastSeen     time.Time
	FSRSCard     fsrs.Card `gorm:"embedded"`
}
