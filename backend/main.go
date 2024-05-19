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
	page := 2

	// Enable CORS middleware
	r.Use(api.CorsMiddleware())

	r.GET("/api/nextQuestion", func(c *gin.Context) {
		api.GetNewQuestion(applicationContext, c, page)
	})

	r.POST("/api/answer", func(c *gin.Context) {
		api.UpdateAnswer(applicationContext, c)

		if page == 2 {
			page = 1
		} else {
			page = 2
		}
	})

	r.Run(applicationContext.Options.BackendPort())
}
