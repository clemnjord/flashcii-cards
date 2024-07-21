package main

import (
	"backend/internal/api"
	"backend/internal/config"
	"backend/internal/database"
	"github.com/gin-gonic/gin"
)

func main() {

	applicationContext := &api.ApplicationContext{
		DB:      database.DatabaseConnection(),
		Options: config.GetInstance(),
	}

	r := gin.Default()

	// Enable CORS middleware
	r.Use(api.CorsMiddleware())

	r.Static("/files", applicationContext.Options.CardsPath())

	r.GET("/api/nextQuestion", func(c *gin.Context) {
		api.GetNewQuestion(applicationContext, c)
	})

	r.POST("/api/answer", func(c *gin.Context) {
		api.UpdateAnswer(applicationContext, c)
	})

	r.Run(applicationContext.Options.BackendPort())
}
