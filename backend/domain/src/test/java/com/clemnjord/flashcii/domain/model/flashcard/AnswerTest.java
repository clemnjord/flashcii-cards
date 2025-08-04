package com.clemnjord.flashcii.domain.model.flashcard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerTest {
    @Test
    void shouldCreateAnswerSuccessfully() {
        Answer answer = new Answer("This is an answer.");
        assertThat(answer.value()).isEqualTo("This is an answer.");
    }

    @Test
    void shouldThrowExceptionWhenAnswerIsNull() {
        assertThatThrownBy(() -> new Answer(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Answer value cannot be null");
    }

    @Test
    void shouldThrowExceptionWhenAnswerIsTooLong() {
        String longAnswer = "a".repeat(501);
        assertThatThrownBy(() -> new Answer(longAnswer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Answer cannot be longer than 500 characters");
    }

    @Test
    void shouldThrowExceptionWhenAnswerIsTooShort() {
        assertThatThrownBy(() -> new Answer("aa"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Answer cannot be shorter than 3 characters");
    }
}
