package models

import (
	"gorm.io/gorm"
)

type Collection struct {
	gorm.Model
	UserID                uint `gorm:"constraint:OnDelete:CASCADE;"`
	Title                 string
	CollectionUrl         string
	UserName              string
	Password              string
	CollectionOwnerThemes string // comma separated list of themes provided by collection owner
	CollectionUserThemes  string // comma separated list of themes provided by collection user
	CollectionVersion     string // version of the collection, git tag or commit hash ?
	Cards                 []Card `gorm:"constraint:OnDelete:CASCADE;"` // Is that useful ?
}
