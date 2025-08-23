package com.clemnjord.flashcii.domain.model.flashcard;

import java.util.Objects;

public record Answer(String value) {
    private static final int MAX_LENGTH = 500;
    private static final int MIN_LENGTH = 3;

    public Answer {
        Objects.requireNonNull(value, "Answer value cannot be null");

        value = value.trim();
        validateAnswer(value);
    }

    private static void validateAnswer(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Answer cannot be longer than " + MAX_LENGTH + " characters");
        }
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Answer cannot be shorter than " + MIN_LENGTH + " characters");
        }
    }

}
