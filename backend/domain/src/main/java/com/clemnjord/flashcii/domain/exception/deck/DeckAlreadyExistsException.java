package com.clemnjord.flashcii.domain.exception.deck;

public class DeckAlreadyExistsException extends RuntimeException {
  public DeckAlreadyExistsException(String message) {
    super(message);
  }
}
