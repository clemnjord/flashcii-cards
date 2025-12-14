package com.clemnjord.flashcii.application.port.input.quiz;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import java.util.Objects;
import java.util.Set;

public record CreateFixedSizedQuizCommand(Integer quizSize, Set<DeckId> includedDecks) {
    public CreateFixedSizedQuizCommand {
        Objects.requireNonNull(quizSize, "Quiz size cannot be null");
        Objects.requireNonNull(includedDecks, "Included decks cannot be null");
    }
}
