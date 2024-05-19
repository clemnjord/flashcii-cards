package config

import (
	"log"
	"os"
	"sync"
)

type Options struct {
	cardsPath    string
	databasePath string
	backendPort  string
}

var instance *Options
var once sync.Once

func GetInstance() *Options {
	once.Do(func() {
		instance = &Options{}

		// Read environment variable
		instance.cardsPath = os.Getenv("FLASHCII_CARDS_PATH")
		if instance.cardsPath == "" {
			log.Fatal("FLASHCII_CARDS_PATH not set")
		}

		instance.databasePath = os.Getenv("FLASHCII_CONFIG_PATH")
		if instance.databasePath == "" {
			log.Fatal("FLASHCII_CONFIG_PATH not set")
		}

		instance.backendPort = os.Getenv("BACKEND_PORT")
		if instance.backendPort == "" {
			log.Fatal("BACKEND_PORT not set")
		}
	})
	return instance
}

func (o *Options) DatabasePath() string {
	return o.databasePath
}

func (o *Options) CardsPath() string {
	return o.cardsPath
}

func (o *Options) BackendPort() string {
	return o.backendPort
}
