package com.clemnjord.flashcii.domain.model.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DeckIdTest {
    @Test
    void shouldThrow_whenUUIDIsNull() {
        assertThatThrownBy(() -> new DeckId(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void generateNewDeckId() {
        // --- Given & When
        DeckId deckId = DeckId.generate();

        // --- Then
        assertThat(deckId).isNotNull();
        assertThat(deckId.uuid()).isNotNull();
    }

    @Test
    void createNewDeckIdWhenFromStringUuidIsValid() {
        // --- Given & When
        DeckId deckId = DeckId.from("12345678-1234-1234-1234-123456789abc");

        // --- Then
        assertThat(deckId).isNotNull();
        assertThat(deckId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void throwExceptionWhenFromStringUuidIsInvalid() {
        // --- Given & When & Then
        assertThatThrownBy(() -> DeckId.from("invalid deck id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid deck ID format: invalid deck id");
    }
}
