package model

import (
	"github.com/open-spaced-repetition/go-fsrs"
	"gorm.io/gorm"
	"time"
)

type UserCard struct {
	gorm.Model
	UserID   uint
	CardID   uint
	LastSeen time.Time
	User     User
	Card     Card
	FSRSCard fsrs.Card `gorm:"embedded"`
}
