package main

import (
	"fmt"
	"log/slog"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

type QuestionContent struct {
	QuestionID int    `json:"id"`
	Data       string `json:"data"`
}

type AnswerContent struct {
	Difficulty string `json:"difficulty"`
}

func main() {
	r := gin.Default()
	page := 2

	// Read environment variable
	flashciiCardsPath := os.Getenv("FLASHCII_CARDS_PATH")
	if flashciiCardsPath == "" {
		slog.Error("FLASHCII_CARDS_PATH not set")
	}

	// Enable CORS middleware
	r.Use(corsMiddleware())

	r.GET("/api/nextQuestion", func(c *gin.Context) {
		getNewQuestion(c, page)
	})

	r.POST("/api/answer", func(c *gin.Context) {
		updateAnswer(c)

		if page == 2 {
			page = 1
		} else {
			page = 2
		}
	})

	port := ":8080" // Update with your desired port
	r.Run(port)
}

// Enable CORS middleware
func corsMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")                            // Allow requests from any origin
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")      // Allow the listed HTTP methods
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization") // Allow the listed headers
		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(http.StatusNoContent)
			return
		}
		c.Next()
	}
}

func updateAnswer(c *gin.Context) {
	var answer AnswerContent

	err := c.BindJSON(&answer)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request body"})
		return
	}

	slog.Debug("Received difficulty:", answer.Difficulty)

	c.JSON(http.StatusOK, gin.H{"message": "Answer received"})
}

func getNewQuestion(c *gin.Context, page int) {
	filePath := fmt.Sprintf("%s/test%d/test%d.html", os.Getenv("FLASHCII_CARDS_PATH"), page, page)

	// Check if the file exists
	_, err := os.Stat(filePath)
	if os.IsNotExist(err) {
		c.JSON(http.StatusNotFound, gin.H{"error": "File not found"})
		return
	}

	// Read the file to the client
	buf, err := os.ReadFile(filePath)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Error reading file"})
		return
	}

	// Send the content to the client
	s := string(buf)

	data := &QuestionContent{
		QuestionID: page,
		Data:       s,
	}

	c.JSON(http.StatusOK, data)
}
