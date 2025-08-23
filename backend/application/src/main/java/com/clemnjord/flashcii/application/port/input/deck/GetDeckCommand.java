package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.DeckId;

import java.util.Objects;

public record GetDeckCommand(DeckId deckId) {

    public GetDeckCommand {
        Objects.requireNonNull(deckId, "Deck ID cannot be null");
    }
}