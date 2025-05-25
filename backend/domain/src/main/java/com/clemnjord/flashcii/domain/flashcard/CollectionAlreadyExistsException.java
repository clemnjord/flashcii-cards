package com.clemnjord.flashcii.domain.flashcard;

public class CollectionAlreadyExistsException extends RuntimeException {
    public CollectionAlreadyExistsException(String message) {
        super(message);
    }
}
