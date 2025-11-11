package com.clemnjord.flashcii.domain.model.user;

import java.util.Objects;

public record Username(String value) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9_-]+$";

    public Username {
        Objects.requireNonNull(value, "Username cannot be null");
        value = value.trim();

        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Username too short (min " + MIN_LENGTH + " characters)");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Username too long (max " + MAX_LENGTH + " characters)");
        }
        if (!value.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException("Username can only contain letters, numbers, underscores and hyphens");
        }
    }
}
