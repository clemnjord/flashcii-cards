package com.clemnjord.flashcii.user.domain.command;

public record CreateUserCommand (String username) {
    public CreateUserCommand {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }
}
