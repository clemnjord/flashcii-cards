package com.clemnjord.flashcii.domain.model.deck;

import java.util.Objects;
import java.util.UUID;

public record DeckId(UUID uuid) {
    public DeckId {
        Objects.requireNonNull(uuid, "Deck ID cannot be null");
    }

    public static DeckId generate() {
        return new DeckId(UUID.randomUUID());
    }

    public static DeckId from(String uuidString) {
        try {
            return new DeckId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid deck ID format: " + uuidString);
        }
    }
}
