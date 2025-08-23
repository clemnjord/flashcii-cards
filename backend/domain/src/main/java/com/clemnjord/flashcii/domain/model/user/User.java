package com.clemnjord.flashcii.domain.model.user;

import lombok.Builder;

import java.util.Objects;

@Builder
public record User(UserId userId, Username username) {
    public User {
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(username, "Username cannot be null");
    }

    public static User createNew(Username username) {
        return new User(UserId.generate(), username);
    }

    public static User restore(UserId userId, Username username) {
        return new User(userId, username);
    }
}
