package com.clemnjord.flashcii.domain.exception.collection;

public class CollectionAlreadyExistsException extends RuntimeException {
  public CollectionAlreadyExistsException(String message) {
    super(message);
  }
}
