package com.clemnjord.flashcii.domain.model.flashcard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlashcardIdTest {
    @Test
    void shouldGenerateNewFlashcardIdSuccessfully() {
        // --- Arrange & Act
        FlashcardId flashcardId = FlashcardId.generate();

        // --- Assert
        assertThat(flashcardId).isNotNull();
    }

    @Test
    void shouldCreateFlashcardSuccessfullyWhenFromStringIsValid() {
        // --- Arrange & Act
        FlashcardId flashcardId = FlashcardId.from("12345678-1234-1234-1234-123456789abc");

        // --- Assert
        assertThat(flashcardId).isNotNull();
        assertThat(flashcardId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void shouldThrowExceptionWhenFromStringIsInvalid() {
        // --- Arrange & Act & Assert
        assertThatThrownBy(() -> FlashcardId.from("invalid flashcard id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid flashcard ID format: invalid flashcard id");
    }
}
