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
        QuizEntity quizEntity = new QuizEntity();
        quizEntity.setQuizId(quiz.getId().uuid());
        quizEntity.setOwner(jpaUserDao
                .findById(ownerId.uuid())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + ownerId.uuid())));
        quizEntity.setCurrentQuestionIndex(quiz.getCurrentQuestionIndex());

        // TODO: Modify jpaFlashcardDao to get all cards in a single query
        List<FlashcardEntity> fEntities = quiz.getFlashcardIds().stream()
                .map(flashcard -> jpaFlashcardDao
                        .findById(new FlashcardEntityId(flashcard.uuid(), ownerId.uuid()))
                        .orElseThrow(() -> new FlashcardNotFoundException("Flashcard not found: " + flashcard.uuid())))
                .toList();

        quizEntity.setFlashcards(fEntities);
        jpaQuizDao.save(quizEntity);
    }

    @Override
    public Optional<FixedSizedQuiz> findById(QuizId quizId) {
        return jpaQuizDao.findById(quizId.uuid()).map(quizEntity -> {
            List<FlashcardId> flashcardIds = quizEntity.getFlashcards().stream()
                    .map(flashcardEntity -> FlashcardId.from(
                            flashcardEntity.getId().getFlashcardId().toString()))
                    .toList();
            return new FixedSizedQuiz(quizId, flashcardIds, quizEntity.getCurrentQuestionIndex());
        });
    }
}
