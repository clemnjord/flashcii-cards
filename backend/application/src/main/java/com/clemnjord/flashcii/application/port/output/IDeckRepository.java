package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.List;
import java.util.Optional;

public interface IDeckRepository {

    void save(Deck deck);

    boolean existsByName(String name);

    boolean existsById(DeckId id);

    Optional<Deck> findByName(String name);

    Optional<Deck> findById(DeckId id);

    List<Deck> findAllByOwnerIdAndNameContainsIgnoreCase(UserId ownerId, String nameFilter);
}
