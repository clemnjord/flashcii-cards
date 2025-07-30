package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaDeckRepository implements IDeckRepository {
    private final SpringDataDeckRepository springRepository;

    public JpaDeckRepository(SpringDataDeckRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Deck save(Deck deck) {
        DeckEntity deckEntity = new DeckEntity();
        deckEntity.setName(deck.getName());
        deckEntity.setDescription(deck.getDescription());

        DeckEntity saved = springRepository.save(deckEntity);

        return new Deck(
                new DeckId(saved.getUUID()),
                saved.getName(),
                saved.getDescription(),
                new UserId(UUID.randomUUID()),
                new HashSet<>());
    }

    @Override
    public boolean existsByName(String name) {

        return springRepository.existsByName(name);
    }

    @Override
    public boolean existsById(DeckId id) {
        return springRepository.existsById(id.uuid());
    }

    @Override
    public Optional<Deck> findByName(String name) {
        return springRepository
                .findByName(name)
                .map(
                        x ->
                                new Deck(
                                        new DeckId(x.getUUID()),
                                        x.getName(),
                                        x.getDescription(),
                                        new UserId(UUID.randomUUID()),
                                        new HashSet<>()));
    }

    @Override
    public Optional<Deck> findById(DeckId id) {
        return springRepository
                .findById(id.uuid())
                .map(
                        x ->
                                new Deck(
                                        new DeckId(x.getUUID()),
                                        x.getName(),
                                        x.getDescription(),
                                        new UserId(UUID.randomUUID()),
                                        new HashSet<>()));
    }
}
