package com.clemnjord.flashcii.domain.model;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import java.util.ArrayList;
import java.util.List;

public record Quiz(QuizId id, DeckId deckId, QuizConfiguration configuration, List<Flashcard> flashcards) {
    public Quiz {
        flashcards = List.copyOf(flashcards);
    }

    public List<Flashcard> flashcards() {
        return new ArrayList<>(flashcards);
    }
}
