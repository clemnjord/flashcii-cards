package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import com.clemnjord.flashcii.infrastructure.persistence.mapper.FlashcardMapper;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JpaFlashcardRepository implements IFlashcardRepository {
    private final JpaFlashcardDao jpaFlashcardDao;
    private final JpaUserDao jpaUserDao;
    private final FlashcardMapper flashcardMapper;

    @Override
    public Optional<Flashcard> findByFlashcardIdAndOwnerId(FlashcardId flashcardId, UserId ownerId) {
        return jpaFlashcardDao
                .findById(new FlashcardEntityId(flashcardId.uuid(), ownerId.uuid()))
                .map(flashcardMapper::toDomain);
    }

    @Override
    public Set<Flashcard> findDueFlashcardsByDeckIdsAndOwnerId(Set<DeckId> deckIds, UserId ownerId) {
        Set<UUID> deckUuidIds = deckIds.stream().map(DeckId::uuid).collect(Collectors.toSet());
        return jpaFlashcardDao.findDueFlashcardsByDeckIdsAndOwnerId(deckUuidIds, ownerId.uuid(), Instant.now()).stream()
                .map(flashcardMapper::toDomain)
                .collect(Collectors.toSet());
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
