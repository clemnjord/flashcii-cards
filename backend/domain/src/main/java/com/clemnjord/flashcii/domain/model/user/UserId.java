package com.clemnjord.flashcii.domain.model.user;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID uuid) {
    public UserId {
        Objects.requireNonNull(uuid, "User ID cannot be null");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(String uuidString) {
        try {
            return new UserId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID format: " + uuidString);
        }
    }
}
