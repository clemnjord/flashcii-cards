package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFlashcardStatisticRepository;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardStatisticEntity;
import io.github.openspacedrepetition.Card;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JpaFlashcardStatisticRepository implements IFlashcardStatisticRepository {
    private final JpaFlashcardStatisticDao jpaFlashcardStatisticDao;

    @Override
    public void save(FlashcardId flashcardId, UserId userId, Card statistic) {
        FlashcardEntityId flashcardEntityId = new FlashcardEntityId(flashcardId.uuid(), userId.uuid());

        FlashcardStatisticEntity flashcardStatisticEntity = new FlashcardStatisticEntity(
                flashcardEntityId,
                statistic.getState(),
                statistic.getStep(),
                statistic.getStability(),
                statistic.getDifficulty(),
                statistic.getDue(),
                statistic.getLastReview());
        jpaFlashcardStatisticDao.save(flashcardStatisticEntity);
    }

    @Override
    public Optional<Card> get(FlashcardId flashcardId, UserId userId) {
        return jpaFlashcardStatisticDao
                .findById(new FlashcardEntityId(flashcardId.uuid(), userId.uuid()))
                .map(entity -> Card.builder()
                        .state(entity.getState())
                        .step(entity.getStep())
                        .stability(entity.getStability())
                        .difficulty(entity.getDifficulty())
                        .due(entity.getDue())
                        .lastReview(entity.getLastReview())
                        .build());
    }
}
