package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.IFixedSizedQuizRepository;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.QuizEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JpaFixedQuizRepository implements IFixedSizedQuizRepository {
    private final JpaFlashcardDao jpaFlashcardDao;
    private final JpaQuizDao jpaQuizDao;
    private final JpaUserDao jpaUserDao;

    @Override
    public void save(FixedSizedQuiz quiz, UserId ownerId) {
        UserEntity owner = jpaUserDao
                .findById(ownerId.uuid())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + ownerId.uuid()));

        List<FlashcardEntityId> flashcardEntityIds = quiz.getFlashcardIds().stream()
                .map(f -> new FlashcardEntityId(f.uuid(), ownerId.uuid()))
                .toList();
        List<FlashcardEntity> fEntities = jpaFlashcardDao.findAllByIdIn(flashcardEntityIds);
        if (fEntities.size() != flashcardEntityIds.size()) {
            throw new FlashcardNotFoundException("Some flashcards not found for the quiz");
        }

        QuizEntity quizEntity = new QuizEntity(quiz.getId().uuid(), owner, fEntities, quiz.getCurrentQuestionIndex());

        jpaQuizDao.save(quizEntity);
    }

    @Override
    public Optional<FixedSizedQuiz> findById(QuizId quizId) {
        return jpaQuizDao.findById(quizId.uuid()).map(quizEntity -> {
            List<FlashcardId> flashcardIds = quizEntity.getFlashcards().stream()
                    .map(flashcardEntity -> FlashcardId.from(
                            flashcardEntity.getId().getFlashcardId().toString()))
                    .toList();
            return new FixedSizedQuiz(
                    quizId,
                    new UserId(quizEntity.getOwner().getId()),
                    flashcardIds,
                    quizEntity.getCurrentQuestionIndex());
        });
    }
}
