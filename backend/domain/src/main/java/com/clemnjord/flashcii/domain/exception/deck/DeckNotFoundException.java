package com.clemnjord.flashcii.domain.exception.deck;

public class DeckNotFoundException extends RuntimeException {
  public DeckNotFoundException(String message) {
    super(message);
  }
}
