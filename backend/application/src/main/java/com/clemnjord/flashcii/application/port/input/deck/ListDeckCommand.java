package com.clemnjord.flashcii.application.port.input.deck;

public record ListDeckCommand(String nameFilter) {
    public ListDeckCommand {
        if (nameFilter == null) {
            nameFilter = "";
        }
    }
}
