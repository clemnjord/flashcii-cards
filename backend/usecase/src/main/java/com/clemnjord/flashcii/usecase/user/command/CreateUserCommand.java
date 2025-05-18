package com.clemnjord.flashcii.usecase.user.command;

public record CreateUserCommand (String username) {
    public CreateUserCommand {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }
}
