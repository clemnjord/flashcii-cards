package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.DeckMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class JpaDeckRepository implements IDeckRepository {
    private final JpaDeckDao jpaDeckDao;
    private final JpaUserDao jpaUserDao;
    private final JpaFlashcardDao jpaFlashcardDao;
    private final DeckMapper deckMapper;

    public JpaDeckRepository(
            JpaDeckDao jpaDeckDao, JpaUserDao jpaUserDao, JpaFlashcardDao jpaFlashcardDao, DeckMapper deckMapper) {
        this.jpaDeckDao = jpaDeckDao;
        this.jpaUserDao = jpaUserDao;
        this.jpaFlashcardDao = jpaFlashcardDao;
        this.deckMapper = deckMapper;
    }

    @Override
    public void save(Deck deck) {
        UserEntity owner = jpaUserDao
                .findById(deck.ownerId().uuid())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + deck.ownerId()));

        jpaDeckDao.save(deckMapper.toEntity(deck, owner));
    }

    @Override
    public boolean existsByNameAndOwnerId(String name, UserId ownerId) {
        return jpaDeckDao.existsByNameAndOwner_Id(name, ownerId.uuid());
    }

    @Override
    public boolean existsByIdAndOwnerId(DeckId id, UserId ownerId) {
        return jpaDeckDao.existsByIdAndOwner_Id(id.uuid(), ownerId.uuid());
    }

    @Override
    public Optional<Deck> findByNameAndOwnerId(String name, UserId ownerId) {
        return jpaDeckDao
                .findByNameAndOwner_Id(name, ownerId.uuid())
                .map(x -> new Deck(
                        new DeckId(x.getId()),
                        x.getName(),
                        x.getDescription(),
                        new UserId(x.getOwner().getId()),
                        new HashSet<>()));
    }

    @Override
    public Optional<Deck> findByIdAndOwnerId(DeckId id, UserId ownerId) {
        Optional<DeckEntity> deckEntity = jpaDeckDao.findByIdAndOwner_Id(id.uuid(), ownerId.uuid());

        // TODO: Can't it be replaced with deckMapper?
        return deckEntity.map(x -> new Deck(
                new DeckId(x.getId()),
                x.getName(),
                x.getDescription(),
                new UserId(x.getOwner().getId()),
                new HashSet<>(x.getFlashcards().stream()
                        .map(FlashcardEntity::getId)
                        .map(i -> FlashcardId.from(i.getFlashcardId().toString()))
                        .collect(Collectors.toSet()))));
    }

    @Override
    public List<Deck> findAllByOwnerIdAndNameContainsIgnoreCase(UserId ownerId, String nameFilter) {
        return jpaDeckDao.findAllByOwner_IdAndNameContainsIgnoreCase(ownerId.uuid(), nameFilter).stream()
                .map(deckMapper::toDomainWithoutFlashcards)
                .toList();
    }

    @Override
    public void addFlashcardToDeck(DeckId deckId, FlashcardId flashcardId, UserId ownerId) {
        FlashcardEntity flashcardEntity = jpaFlashcardDao
                .findById(new FlashcardEntityId(flashcardId.uuid(), ownerId.uuid()))
                .orElseThrow(() ->
                        new FlashcardNotFoundException("Flashcard must exist before adding to deck: " + flashcardId));

        DeckEntity deckEntity = jpaDeckDao
                .findByIdAndOwner_Id(deckId.uuid(), ownerId.uuid())
                .orElseThrow(
                        () -> new DeckNotFoundException("Deck must exist before adding flashcard: " + deckId.uuid()));

        deckEntity.addFlashcard(flashcardEntity);
        jpaDeckDao.save(deckEntity);
    }
}
