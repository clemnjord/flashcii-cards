package com.clemnjord.flashcii.domain.model;

import java.util.Objects;

public record Answer(String value) {
    public Answer {
        Objects.requireNonNull(value, "Answer value cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Answer value cannot be blank");
        }
    }
}
