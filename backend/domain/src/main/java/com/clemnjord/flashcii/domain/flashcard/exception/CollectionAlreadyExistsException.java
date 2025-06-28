package com.clemnjord.flashcii.domain.flashcard.exception;

public class CollectionAlreadyExistsException extends RuntimeException {
  public CollectionAlreadyExistsException(String message) {
    super(message);
  }
}
