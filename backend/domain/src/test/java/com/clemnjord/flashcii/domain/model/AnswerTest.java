package com.clemnjord.flashcii.domain.model;

import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AnswerTest {
  @Test
  void shouldThrowIllegalExceptionWhenAnswerIsBlank() {
    Assertions.assertThatThrownBy(() -> new Answer(""))
        .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Answer cannot be shorter than 3 characters");
  }

  @Test
  void shouldThrowNullPointerExceptionWhenAnswerIsNull() {
    Assertions.assertThatThrownBy(() -> new Answer(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Answer value cannot be null");
  }
}
