package model

import (
	"gorm.io/gorm"
)

type Collection struct {
	gorm.Model
	Title    string
	RepoUrl  string
	UserName string
	Password string
}
