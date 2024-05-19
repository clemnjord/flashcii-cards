package api

import (
	"backend/internal/model"
	"fmt"
	"github.com/gin-gonic/gin"
	"log/slog"
	"net/http"
	"os"
)

type questionContent struct {
	QuestionID int    `json:"id"`
	Data       string `json:"data"`
}

type answerContent struct {
	Difficulty string `json:"difficulty"`
}

func UpdateAnswer(appC *ApplicationContext, c *gin.Context) {
	var answer answerContent

	err := c.BindJSON(&answer)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid request body"})
		return
	}

	slog.Debug("Received difficulty:", answer.Difficulty)

	c.JSON(http.StatusOK, gin.H{"message": "Answer received"})
}

func GetNewQuestion(appC *ApplicationContext, c *gin.Context, page int) {
	var nextCard model.Card
	tx := appC.DB.First(&nextCard, "id = ?", page)
	if tx.Error != nil {
		slog.Error("Error getting card:", tx.Error)
	}

	filePath := fmt.Sprintf("%s/%s/card.html", appC.Options.CardsPath(), nextCard.DataPath)

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

	data := &questionContent{
		QuestionID: page,
		Data:       s,
	}

	c.JSON(http.StatusOK, data)
}
