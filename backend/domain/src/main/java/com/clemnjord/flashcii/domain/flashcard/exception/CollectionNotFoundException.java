package com.clemnjord.flashcii.domain.flashcard.exception;

import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;

public class CollectionNotFoundException extends RuntimeException {
  public CollectionNotFoundException(CollectionId collectionId) {
    super("Collection not found with ID: " + collectionId.uuid());
  }
}
