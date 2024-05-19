package api

import (
	"backend/internal/config"
	"gorm.io/gorm"
)

type ApplicationContext struct {
	DB      *gorm.DB
	Options *config.Options
}
