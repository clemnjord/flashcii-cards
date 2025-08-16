package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Question;

import java.util.Objects;

public record CreateFlashcardCommand(DeckId deckId, Question question, Answer answer) {

    public CreateFlashcardCommand {
        Objects.requireNonNull(deckId, "Deck ID cannot be null");
        Objects.requireNonNull(question, "Question cannot be null");
        Objects.requireNonNull(answer, "Answer cannot be null");
    }
}
