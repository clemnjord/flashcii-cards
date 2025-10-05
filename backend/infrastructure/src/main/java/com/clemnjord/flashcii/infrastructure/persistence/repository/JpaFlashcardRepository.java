package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.FlashcardMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaFlashcardRepository implements IFlashcardRepository {
    private final JpaFlashcardDao jpaFlashcardDao;
    private final JpaUserDao jpaUserDao;
    private final FlashcardMapper flashcardMapper;

    public JpaFlashcardRepository(
            JpaFlashcardDao jpaFlashcardDao,
            JpaUserDao jpaUserDao,
            FlashcardMapper flashcardMapper) {
        this.jpaFlashcardDao = jpaFlashcardDao;
        this.jpaUserDao = jpaUserDao;
        this.flashcardMapper = flashcardMapper;
    }

    @Override
    public Optional<Flashcard> findByFlashcardIdAndOwnerId(FlashcardId flashcardId, UserId ownerId) {
        return jpaFlashcardDao
                .findById(new FlashcardEntityId(flashcardId.uuid(), ownerId.uuid()))
                .map(flashcardMapper::toDomain);
    }

    @Override
    public void save(Flashcard flashcard, UserId ownerId) {
        UserEntity userEntity = jpaUserDao
                .findById(ownerId.uuid())
                .orElseThrow(() -> new UserNotFoundException("User not found when saving a Flashcard"));

        FlashcardEntity flashcardEntity = flashcardMapper.toEntity(flashcard, userEntity);

        jpaFlashcardDao.save(flashcardEntity);
    }
}
