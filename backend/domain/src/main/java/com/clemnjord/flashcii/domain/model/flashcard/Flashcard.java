package com.clemnjord.flashcii.domain.model.flashcard;

import lombok.Builder;

import java.util.Objects;

public record Flashcard(FlashcardId flashcardId, Question question, Answer answer) {

  @Builder
  public Flashcard {
    Objects.requireNonNull(question, "Question cannot be null");
    Objects.requireNonNull(answer, "Answer cannot be null");
  }
}
