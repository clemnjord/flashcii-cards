package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;

import java.util.Objects;

@ApplicationService
public interface ICreateFlashcardUseCase {
    Flashcard execute(CreateFlashcardCommand command);

    record CreateFlashcardCommand(DeckId deckId, String question, String answer) {

        public CreateFlashcardCommand {
            Objects.requireNonNull(deckId, "Deck ID cannot be null");
            Objects.requireNonNull(question, "Question cannot be null");
            Objects.requireNonNull(answer, "Answer cannot be null");
        }
    }
}
