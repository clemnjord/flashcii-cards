package main

import (
	"io"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()

	// Enable CORS middleware
	r.Use(func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "*")                            // Allow requests from any origin
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")      // Allow the listed HTTP methods
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization") // Allow the listed headers
		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(http.StatusNoContent)
			return
		}
		c.Next()
	})

	r.GET("/api/file/:filename", func(c *gin.Context) {
		filename := c.Param("filename")
		filePath := os.Getenv("FLASHCII_CARDS_PATH") + "/" + filename + "/" + filename + ".html"

		// Check if the file exists
		_, err := os.Stat(filePath)
		if os.IsNotExist(err) {
			c.JSON(http.StatusNotFound, gin.H{"error": "File not found"})
			return
		}

		// Open the file
		file, err := os.Open(filePath)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Unable to open file"})
			return
		}
		defer file.Close()

		// Set appropriate content type based on file extension
		contentType := http.DetectContentType([]byte(filename))
		c.Header("Content-Type", contentType)

		// Stream the file to the client
		_, err = io.Copy(c.Writer, file)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Error streaming file"})
			return
		}
	})

	port := ":8080" // Update with your desired port
	r.Run(port)
}
