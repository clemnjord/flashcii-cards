package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.List;
import java.util.Optional;

public interface IDeckRepository {

    void save(Deck deck);

    boolean existsByNameAndOwnerId(String name, UserId ownerId);

    boolean existsByIdAndOwnerId(DeckId id, UserId ownerId);

    Optional<Deck> findByIdAndOwnerId(DeckId id, UserId ownedId);

    Optional<Deck> findByNameAndOwnerId(String name, UserId ownerId);

    List<Deck> findAllByOwnerIdAndNameContainsIgnoreCase(UserId ownerId, String nameFilter);

    void addFlashcardToDeck(DeckId deckId, FlashcardId flashcardId, UserId userId);
}
