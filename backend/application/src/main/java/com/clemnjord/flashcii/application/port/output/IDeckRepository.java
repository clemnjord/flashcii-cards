package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;

import java.util.Optional;

public interface IDeckRepository {

    Deck save(Deck deck);

    boolean existsByName(String name);

    boolean existsById(DeckId id);

    Optional<Deck> findByName(String name);

    Optional<Deck> findById(DeckId id);
}
