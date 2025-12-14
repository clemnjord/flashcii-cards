package com.clemnjord.flashcii.domain.model.quiz;

import java.util.Objects;
import java.util.UUID;

public record QuizId(UUID uuid) {
    public QuizId {
        Objects.requireNonNull(uuid, "Quiz ID cannot be null");
    }

    public static QuizId generate() {
        return new QuizId(UUID.randomUUID());
    }

    public static QuizId from(String uuidString) {
        try {
            return new QuizId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Quiz ID format: " + uuidString);
        }
    }
}
