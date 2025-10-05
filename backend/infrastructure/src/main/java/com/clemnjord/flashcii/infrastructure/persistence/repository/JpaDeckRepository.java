package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.*;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.DeckMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaDeckRepository implements IDeckRepository {
    private final JpaDeckDao jpaDeckDao;
    private final JpaUserDao jpaUserDao;
    private final JpaFlashcardDao jpaFlashcardDao;
    private final JpaDeckFlashcardDao jpaDeckFlashcardDao;
    private final DeckMapper deckMapper;

    public JpaDeckRepository(
            JpaDeckDao jpaDeckDao,
            JpaUserDao jpaUserDao,
            JpaFlashcardDao jpaFlashcardDao,
            JpaDeckFlashcardDao jpaDeckFlashcardDao,
            DeckMapper deckMapper) {
        this.jpaDeckDao = jpaDeckDao;
        this.jpaUserDao = jpaUserDao;
        this.jpaFlashcardDao = jpaFlashcardDao;
        this.jpaDeckFlashcardDao = jpaDeckFlashcardDao;
        this.deckMapper = deckMapper;
    }

    @Override
    public void save(Deck deck) {
        UserEntity owner = jpaUserDao
                .findById(deck.ownerId().uuid())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + deck.ownerId()));

        jpaDeckDao.save(deckMapper.toEntity(deck, owner));
    }

    @Override
    public boolean existsByNameAndOwnerId(String name, UserId ownerId) {

        return jpaDeckDao.existsByNameAndOwner_Uuid(name, ownerId.uuid());
    }

    @Override
    public boolean existsByIdAndOwnerId(DeckId id, UserId ownerId) {
        return jpaDeckDao.existsByUuidAndOwner_Uuid(id.uuid(), ownerId.uuid());
    }

    @Override
    public Optional<Deck> findByNameAndOwnerId(String name, UserId ownerId) {
        return jpaDeckDao
                .findByNameAndOwner_Uuid(name, ownerId.uuid())
                .map(x -> Deck.restore(
                        new DeckId(x.getUUID()),
                        x.getName(),
                        x.getDescription(),
                        new UserId(x.getOwner().getUuid()),
                        new HashSet<>()));
    }

    @Override
    public Optional<Deck> findByIdAndOwnerId(DeckId id, UserId ownerId) {
        return jpaDeckDao
                .findByUuidAndOwner_Uuid(id.uuid(), ownerId.uuid())
                .map(x -> Deck.restore(
                        new DeckId(x.getUUID()),
                        x.getName(),
                        x.getDescription(),
                        new UserId(x.getOwner().getUuid()),
                        new HashSet<>()));
    }

    @Override
    public List<Deck> findAllByOwnerIdAndNameContainsIgnoreCase(UserId ownerId, String nameFilter) {
        return jpaDeckDao.findAllByOwner_UuidAndNameContainsIgnoreCase(ownerId.uuid(), nameFilter).stream()
                .map(deckMapper::toDomainWithoutFlashcards)
                .toList();
    }

    @Override
    public void addFlashcardToDeck(DeckId deckId, FlashcardId flashcardId, UserId ownerId) {
        FlashcardEntity flashcardEntity = jpaFlashcardDao
                .findById(new FlashcardEntityId(flashcardId.uuid(), ownerId.uuid()))
                .orElseThrow(() ->
                        new IllegalStateException("Flashcard must exist before adding to deck: " + flashcardId));

        DeckEntity deckEntity = jpaDeckDao
                .findByUuidAndOwner_Uuid(deckId.uuid(), ownerId.uuid())
                .orElseThrow(() -> new IllegalStateException("Deck must exist before adding flashcard: " + deckId.uuid()));

        boolean existsInDeck = jpaDeckFlashcardDao.existsByDeck_UuidAndFlashcard_Id_FlashcardIdAndFlashcard_Id_OwnerId(
                deckId.uuid(), flashcardId.uuid(), ownerId.uuid());

        if (!existsInDeck) {
            DeckFlashcardEntity deckFlashcardEntity = new DeckFlashcardEntity(deckEntity, flashcardEntity);
            jpaDeckFlashcardDao.save(deckFlashcardEntity);
        }
    }
}
