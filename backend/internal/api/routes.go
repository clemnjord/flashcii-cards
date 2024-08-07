package api

import (
	"backend/internal/models"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/open-spaced-repetition/go-fsrs"
	"log/slog"
	"net/http"
	"os"
	"time"
)

type nextOccurrence struct {
	AgainTime string `json:"againTime"`
	HardTime  string `json:"hardTime"`
	GoodTime  string `json:"goodTime"`
	EasyTime  string `json:"easyTime"`
}
type cardContent struct {
	CardId         uint           `json:"id"`
	Data           string         `json:"data"`
	NextOccurrence nextOccurrence `json:"nextOccurrence"`
}

type answerContent struct {
	CardId     uint   `json:"cardId"`
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
	var card models.Card
	p := fsrs.DefaultParam()
	now := time.Now()
	appC.DB.Where("id = ?", answer.CardId).First(&card)

	schedulingCards := p.Repeat(card.FSRSCard, now)

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
	card.FSRSCard = fsrsCard

	appC.DB.Save(&card)

	slog.Debug("Received difficulty:", answer.Difficulty)

	c.JSON(http.StatusOK, gin.H{"message": "Answer received"})
}

func GetNewQuestion(appC *ApplicationContext, c *gin.Context) {
	var card models.Card
	p := fsrs.DefaultParam()
	now := time.Now()

	tx := appC.DB.Joins("JOIN collections ON collections.id = cards.collection_id").
		Where("collections.user_id = ? AND due < ?", 1, now).
		Order("due asc").
		First(&card)

	if tx.Error != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "No cards to show"})
		return
	}

	schedulingCards := p.Repeat(card.FSRSCard, now)

	nextOccurrence := &nextOccurrence{
		AgainTime: schedulingCards[fsrs.Again].Card.Due.Sub(now).String(),
		HardTime:  schedulingCards[fsrs.Hard].Card.Due.Sub(now).String(),
		GoodTime:  schedulingCards[fsrs.Good].Card.Due.Sub(now).String(),
		EasyTime:  schedulingCards[fsrs.Easy].Card.Due.Sub(now).String(),
	}

	card.LastSeen = time.Now()
	appC.DB.Save(&card)

	filePath := fmt.Sprintf("%s/%s/card.adoc", appC.Options.CardsPath(), card.DataPath)

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

	data := &cardContent{
		CardId:         card.ID,
		Data:           s,
		NextOccurrence: *nextOccurrence,
	}

	c.JSON(http.StatusOK, data)
}
