package api

import (
	"backend/internal/model"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/open-spaced-repetition/go-fsrs"
	"log/slog"
	"net/http"
	"os"
	"time"
)

type questionContent struct {
	QuestionID uint   `json:"id"`
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

	// Retrieve card
	var userCard model.UserCard
	p := fsrs.DefaultParam()
	now := time.Now()
	appC.DB.Where("user_id = ?", 1).Order("due asc").Preload("Card").First(&userCard)

	schedulingCards := p.Repeat(userCard.FSRSCard, now)

	var rating fsrs.Rating
	if answer.Difficulty == "HARD" {
		rating = fsrs.Hard
	} else if answer.Difficulty == "GOOD" {
		rating = fsrs.Good
	} else if answer.Difficulty == "EASY" {
		rating = fsrs.Easy
	} else {
		rating = fsrs.Again
	}
	fsrsCard := schedulingCards[rating].Card
	userCard.FSRSCard = fsrsCard

	appC.DB.Save(&userCard)

	slog.Debug("Received difficulty:", answer.Difficulty)

	c.JSON(http.StatusOK, gin.H{"message": "Answer received"})
}

func GetNewQuestion(appC *ApplicationContext, c *gin.Context) {
	var userCard model.UserCard
	p := fsrs.DefaultParam()
	now := time.Now()
	appC.DB.Where("user_id = ? AND due < ?", 1, now).Order("due asc").Preload("Card").First(&userCard)

	schedulingCards := p.Repeat(userCard.FSRSCard, now)

	// These values should be sent to the frontend to be used as tooltips for the buttons
	slog.Info("Next if Again: " + schedulingCards[fsrs.Again].Card.Due.Sub(now).String())
	slog.Info("Next if Hard: " + schedulingCards[fsrs.Hard].Card.Due.Sub(now).String())
	slog.Info("Next if Good: " + schedulingCards[fsrs.Good].Card.Due.Sub(now).String())
	slog.Info("Next if Easy: " + schedulingCards[fsrs.Easy].Card.Due.Sub(now).String())

	userCard.LastSeen = time.Now()
	appC.DB.Save(&userCard)

	filePath := fmt.Sprintf("%s/%s/card.html", appC.Options.CardsPath(), userCard.Card.DataPath)

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
		QuestionID: userCard.CardID,
		Data:       s,
	}

	c.JSON(http.StatusOK, data)
}
