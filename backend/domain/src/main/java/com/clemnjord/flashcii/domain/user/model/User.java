package com.clemnjord.flashcii.domain.user.model;

import lombok.Builder;

@Builder
public record User(UserId userId,
                   String username) {

    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (username.length() > 255) {
            throw new IllegalArgumentException("Username cannot be longer than 255 characters");
        }
        if (!username.matches("^\\w+$")) {
            throw new IllegalArgumentException("Username can only contain alphanumeric characters and underscores");
        }
    }
}
