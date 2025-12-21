package com.clemnjord.flashcii.domain.model.quiz;

import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;

@Getter
public class FixedSizedQuiz {
    private final QuizId id;
    private final UserId ownerId;
    private final List<FlashcardId> flashcardIds;
    private int currentQuestionIndex;

    public FixedSizedQuiz(QuizId id, UserId ownerId, List<FlashcardId> flashcardIds) {
        this(id, ownerId, flashcardIds, 0);
    }

    public FixedSizedQuiz(QuizId id, UserId ownerId, List<FlashcardId> flashcardIds, int currentQuestionIndex) {
        this.id = Objects.requireNonNull(id, "Quiz ID cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId, "Owner ID cannot be null");

        Objects.requireNonNull(flashcardIds, "Flashcard IDs cannot be null");
        if (flashcardIds.isEmpty()) {
            throw new IllegalArgumentException("Flashcard IDs cannot be empty");
        }
        this.flashcardIds = List.copyOf(flashcardIds);
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public static FixedSizedQuiz createNew(UserId ownerId, List<FlashcardId> flashcardIds) {
        return new FixedSizedQuiz(QuizId.generate(), ownerId, flashcardIds);
    }

    public Optional<FlashcardId> getCurrentFlashcardId() {
        if (isFinished()) {
            return Optional.empty();
        }
        return Optional.of(flashcardIds.get(currentQuestionIndex));
    }

    public void answerCurrentQuestion(FlashcardId answeringFlashcardId) {
        getCurrentFlashcardId().ifPresent(currentFlashcardId -> {
            if (!currentFlashcardId.equals(answeringFlashcardId)) {
                throw new IllegalArgumentException(
                        "Answering flashcard ID does not match the current question's flashcard ID");
            }
        });

        if (isFinished()) {
            return;
        }
        currentQuestionIndex++;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= flashcardIds.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FixedSizedQuiz fixedSizedQuiz = (FixedSizedQuiz) o;
        return Objects.equals(id, fixedSizedQuiz.id)
                && Objects.equals(ownerId, fixedSizedQuiz.ownerId)
                && Objects.equals(flashcardIds, fixedSizedQuiz.flashcardIds)
                && currentQuestionIndex == fixedSizedQuiz.currentQuestionIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, flashcardIds, currentQuestionIndex);
    }
}
