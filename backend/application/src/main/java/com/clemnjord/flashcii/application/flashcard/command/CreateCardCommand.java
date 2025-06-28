package com.clemnjord.flashcii.application.flashcard.command;

import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;

public record CreateCardCommand(CollectionId collectionId, String question, String answer) {

  public CreateCardCommand {
    if (question == null || question.isBlank()) {
      throw new IllegalArgumentException("Question cannot be null or blank");
    }
    if (answer == null || answer.isBlank()) {
      throw new IllegalArgumentException("Answer cannot be null or blank");
    }
  }
}
