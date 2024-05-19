package model

import "gorm.io/gorm"

type Card struct {
	gorm.Model
	Title    string
	DataPath string
}
