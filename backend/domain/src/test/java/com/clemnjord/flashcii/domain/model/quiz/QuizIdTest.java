package com.clemnjord.flashcii.domain.model.quiz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuizIdTest {

    @Test
    void shouldThrow_whenUUIDIsNull() {
        assertThatThrownBy(() -> new QuizId(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void quizIdGenerateShouldSucceed() {
        // --- Given & When
        QuizId quizId = QuizId.generate();

        // Then
        assertThat(quizId).isNotNull();
        assertThat(quizId.uuid()).isNotNull();
    }

    @Test
    @DisplayName("Test that creating a QuizId from a valid string succeeds")
    void quizIdFromShouldSucceed_whenInputStringValid() {
        // --- Given & When
        QuizId quizId = QuizId.from("123e4567-e89b-12d3-a456-426614174000");

        // --- Then
        assertThat(quizId).isNotNull();
        assertThat(quizId.uuid()).hasToString("123e4567-e89b-12d3-a456-426614174000");
    }

    @Test
    @DisplayName("Test that creating a QuizId from an invalid string throws")
    void quizIdFromShouldThrow_whenInputStringInValid() {
        // --- Given & When & Then
        assertThatThrownBy(() -> QuizId.from("invalid deck id")).isInstanceOf(IllegalArgumentException.class);
    }
}
