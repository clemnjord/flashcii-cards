package com.clemnjord.flashcii.application.port.input.quiz;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import java.util.Objects;
import java.util.Set;

public record CreateFixedSizedQuizCommand(Set<DeckId> includedDecks) {
    public CreateFixedSizedQuizCommand {
        Objects.requireNonNull(includedDecks, "Included decks cannot be null");
    }
}
