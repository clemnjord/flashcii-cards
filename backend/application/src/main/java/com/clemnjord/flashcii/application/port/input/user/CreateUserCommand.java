package com.clemnjord.flashcii.application.port.input.user;


import java.util.Objects;

public record CreateUserCommand(String username) {
    public CreateUserCommand {
        Objects.requireNonNull(username, "Username cannot be null");
    }
}

