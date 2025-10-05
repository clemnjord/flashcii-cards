package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Objects;

public record AddCardToDeckCommand(DeckId deckId, FlashcardId flashcardId, UserId ownerId) {
    public AddCardToDeckCommand {
        Objects.requireNonNull(deckId, "Deck ID cannot be null");
        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");
        Objects.requireNonNull(ownerId, "User ID cannot be null");
    }
}
