package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.Optional;
import java.util.Set;

public interface IFlashcardRepository {
    void save(Flashcard flashcard, UserId userId);

    Optional<Flashcard> findByFlashcardIdAndOwnerId(FlashcardId flashcardId, UserId ownerId);

    Set<Flashcard> findDueFlashcardsByDeckIdsAndOwnerId(Set<DeckId> deckIds, UserId ownerId);
}
