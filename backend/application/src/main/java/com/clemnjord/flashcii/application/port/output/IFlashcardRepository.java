package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Optional;

public interface IFlashcardRepository {
    void save(Flashcard flashcard, DeckId deckId, UserId userId);

    Optional<Flashcard> findById(FlashcardId id);

    boolean existsByQuestionAndDeckId(Question question, DeckId deckId);
}
