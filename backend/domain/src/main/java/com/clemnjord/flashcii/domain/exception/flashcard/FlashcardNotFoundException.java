package com.clemnjord.flashcii.domain.exception.flashcard;

public class FlashcardNotFoundException extends RuntimeException {
    public FlashcardNotFoundException(String message) {
        super(message);
    }
}
