package com.clemnjord.flashcii.domain.model.flashcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AnswerTest {
    @Test
    void createNewAnswerWhenInputIsValid() {
        Answer answer = new Answer("This is an answer.");
        assertThat(answer.value()).isEqualTo("This is an answer.");
    }

    @Test
    void throwExceptionWhenAnswerIsNull() {
        assertThatThrownBy(() -> new Answer(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Answer value cannot be null");
    }

    @Test
    void throwExceptionWhenAnswerIsTooLong() {
        String longAnswer = "a".repeat(501);
        assertThatThrownBy(() -> new Answer(longAnswer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Answer cannot be longer than 500 characters");
    }

    @Test
    void throwExceptionWhenAnswerIsTooShort() {
        assertThatThrownBy(() -> new Answer("aa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Answer cannot be shorter than 3 characters");
    }
}
