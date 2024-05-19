package api

import (
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
	filePath := fmt.Sprintf("%s/test%d/test%d.html", appC.Options.CardsPath(), page, page)

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
