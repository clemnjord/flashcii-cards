package api

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

// Enable CORS middleware
func CorsMiddleware() gin.HandlerFunc {
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
