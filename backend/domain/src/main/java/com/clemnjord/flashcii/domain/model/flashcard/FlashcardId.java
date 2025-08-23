package com.clemnjord.flashcii.domain.model.flashcard;

import java.util.Objects;
import java.util.UUID;

public record FlashcardId(UUID uuid) {
    public FlashcardId {
        Objects.requireNonNull(uuid, "Flashcard ID cannot be null");
    }

    public static FlashcardId generate() {
        return new FlashcardId(UUID.randomUUID());
    }

    public static FlashcardId from(String uuidString) {
        try {
            return new FlashcardId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid flashcard ID format: " + uuidString);
        }
    }
}
