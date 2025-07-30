package com.clemnjord.flashcii.domain.model.flashcard;

import java.util.Objects;

public record Question(String value) {
    private static final int MAX_LENGTH = 500;
    private static final int MIN_LENGTH = 3;

    public Question {
        Objects.requireNonNull(value, "Question value cannot be null");

        String trimmed = value.trim();
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Question cannot be longer than " + MAX_LENGTH + " characters");
        }
        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Question cannot be shorter than " + MIN_LENGTH + " characters");
        }

        value = trimmed;
    }
}
