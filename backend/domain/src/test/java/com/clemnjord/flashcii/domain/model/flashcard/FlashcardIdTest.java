package com.clemnjord.flashcii.domain.model.flashcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FlashcardIdTest {

    @Test
    void shouldThrow_whenUUIDIsNull() {
        assertThatThrownBy(() -> new FlashcardId(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void generateNewFlashcardId() {
        // --- Given & When
        FlashcardId flashcardId = FlashcardId.generate();

        // --- Then
        assertThat(flashcardId).isNotNull();
    }

    @Test
    void createNewFlashcardIdWhenFromStringUuidIsValid() {
        // --- Given & When
        FlashcardId flashcardId = FlashcardId.from("12345678-1234-1234-1234-123456789abc");

        // --- Then
        assertThat(flashcardId).isNotNull();
        assertThat(flashcardId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void throwExceptionWhenFromStringUuidIsInvalid() {
        // --- Given & When & Then
        assertThatThrownBy(() -> FlashcardId.from("invalid flashcard id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid flashcard ID format: invalid flashcard id");
    }
}
