package com.clemnjord.flashcii.domain.model;

import java.util.Objects;

public record Question(String value) {
    public Question {
        Objects.requireNonNull(value, "Question value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Question value cannot be blank");
        }
        if (value.length() > 500) {
            throw new IllegalArgumentException("Question cannot be longer than 500 characters");
        }
    }
}
