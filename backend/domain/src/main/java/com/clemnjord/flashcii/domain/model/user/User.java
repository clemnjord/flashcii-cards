package com.clemnjord.flashcii.domain.model.user;

import java.util.Objects;
import lombok.Builder;

@Builder
public record User(UserId userId, Username username) {
    public User {
        Objects.requireNonNull(userId, "User ID cannot be null");
        Objects.requireNonNull(username, "Username cannot be null");
    }

    public static User createNew(Username username) {
        return new User(UserId.generate(), username);
    }
}
