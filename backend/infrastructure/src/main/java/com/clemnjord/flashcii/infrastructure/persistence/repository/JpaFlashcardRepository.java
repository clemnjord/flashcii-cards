package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckFlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.FlashcardMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFlashcardRepository implements IFlashcardRepository {
    private final JpaFlashcardDao jpaFlashcardDao;
    private final JpaDeckDao jpaDeckDao;
    private final JpaUserDao jpaUserDao;
    private final JpaDeckFlashcardDao jpaDeckFlashcardDao;
    private final FlashcardMapper flashcardMapper;

    public JpaFlashcardRepository(
            JpaFlashcardDao jpaFlashcardDao,
            JpaDeckDao jpaDeckDao,
            JpaUserDao jpaUserDao,
            JpaDeckFlashcardDao jpaDeckFlashcardDao,
            FlashcardMapper flashcardMapper) {
        this.jpaFlashcardDao = jpaFlashcardDao;
        this.jpaDeckDao = jpaDeckDao;
        this.jpaUserDao = jpaUserDao;
        this.jpaDeckFlashcardDao = jpaDeckFlashcardDao;
        this.flashcardMapper = flashcardMapper;
    }

    @Override
    public Optional<Flashcard> findById(FlashcardId flashcardId) {
        return jpaFlashcardDao.findById(flashcardId.uuid()).map(flashcardMapper::toDomain);
    }

    @Override
    public void save(Flashcard flashcard, DeckId deckId, UserId ownerId) {
        DeckEntity deckEntity = jpaDeckDao
                .findByUuidAndOwner_Uuid(deckId.uuid(), ownerId.uuid())
                .orElseThrow(() -> new DeckNotFoundException("Deck not found when saving a Flashcard"));

        UserEntity userEntity = jpaUserDao
                .findById(ownerId.uuid())
                .orElseThrow(() -> new UserNotFoundException("User not found when saving a Flashcard"));

        FlashcardEntity flashcardEntity = flashcardMapper.toEntity(flashcard, userEntity);

        jpaFlashcardDao.save(flashcardEntity);

        boolean existsInDeck = jpaDeckFlashcardDao.existsByDeck_UuidAndFlashcard_Id_FlashcardIdAndFlashcard_Id_OwnerId(
                deckId.uuid(), flashcard.flashcardId().uuid(), ownerId.uuid());

        if (!existsInDeck) {
            DeckFlashcardEntity deckFlashcardEntity = new DeckFlashcardEntity(deckEntity, flashcardEntity);
            jpaDeckFlashcardDao.save(deckFlashcardEntity);
        }
    }
}
