package com.clemnjord.flashcii.domain.model;

import java.util.Objects;
import lombok.Builder;

public record Card(CardId cardId, Question question, Answer answer) {

  @Builder
  public Card {
    Objects.requireNonNull(question, "Question cannot be null");
    Objects.requireNonNull(answer, "Answer cannot be null");
  }
}
