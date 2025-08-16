package com.clemnjord.flashcii.application.port.input.deck;

import java.util.List;
import java.util.Objects;

public record CreateDeckCommand(String name, String description, List<String> tags) {

    public CreateDeckCommand(String name, String description) {
        this(name, description, List.of());
    }

    public CreateDeckCommand {
        Objects.requireNonNull(name, "Deck name cannot be null");
        Objects.requireNonNull(description, "Deck description cannot be null");
        Objects.requireNonNull(tags, "Deck tags cannot be null");
    }
}