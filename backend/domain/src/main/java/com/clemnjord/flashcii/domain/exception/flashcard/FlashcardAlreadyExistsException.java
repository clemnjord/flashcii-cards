package com.clemnjord.flashcii.domain.exception.flashcard;

public class FlashcardAlreadyExistsException extends RuntimeException {
  public FlashcardAlreadyExistsException(String message) {
    super(message);
  }
}
