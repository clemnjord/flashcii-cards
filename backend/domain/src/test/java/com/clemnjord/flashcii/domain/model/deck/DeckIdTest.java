package com.clemnjord.flashcii.domain.model.deck;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeckIdTest {

    @Test
    void shouldGenerateNewDeckIdSuccessfully() {
        // --- Arrange & Act
        DeckId deckId = DeckId.generate();

        // --- Assert
        assertThat(deckId).isNotNull();
    }

    @Test
    void shouldCreateDeckSuccessfullyWhenFromStringIsValid() {
        // --- Arrange & Act
        DeckId deckId = DeckId.from("12345678-1234-1234-1234-123456789abc");

        // --- Assert
        assertThat(deckId).isNotNull();
        assertThat(deckId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void shouldThrowExceptionWhenFromStringIsInvalid() {
        // --- Arrange & Act & Assert
        assertThatThrownBy(() -> DeckId.from("invalid deck id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid deck ID format: invalid deck id");
    }
}
