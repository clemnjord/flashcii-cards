package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.output.IFixedSizedQuizRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardStatisticRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.quiz.QuizNotFoundException;
import com.clemnjord.flashcii.domain.model.user.User;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.Scheduler;
import lombok.AllArgsConstructor;

@ApplicationService
@ApplicationTransactional
@AllArgsConstructor
public class RateFlashcardUseCaseImpl implements RateFlashcardUseCase {
    private final Scheduler scheduler = Scheduler.builder().build();
    private final IUserContextService userContextService;
    private final IFlashcardStatisticRepository flashcardStatisticRepository;
    private final IFlashcardRepository flashcardRepository;
    private final IFixedSizedQuizRepository quizRepository;

    @Override
    public void execute(RateFlashcardCommand command) {
        User currentUser = userContextService.getCurrentUser();

        // Verify that the quiz exists and is owned by the current user
        var quiz = quizRepository
                .findById(command.quizId())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        if (!quiz.getOwnerId().equals(currentUser.userId())) {
            throw new QuizNotFoundException("Quiz not found");
        }

        if (!quiz.getFlashcardIds().contains(command.flashcardId())) {
            throw new FlashcardNotFoundException("Flashcard not found in quiz");
        }

        // Verify that the flashcard exists and is owned by the current user
        var flashcard = flashcardRepository
                .findByFlashcardIdAndOwnerId(command.flashcardId(), currentUser.userId())
                .orElseThrow(() -> new FlashcardNotFoundException("Flashcard not found"));

        // Answer the current question in the quiz
        quiz.answerCurrentQuestion(flashcard.flashcardId());
        quizRepository.save(quiz, currentUser.userId());

        // Retrieve existing statistics or create a new one if it doesn't exist
        Card card = flashcardStatisticRepository
                .get(command.flashcardId(), currentUser.userId())
                .orElse(Card.builder().build());

        Card updatedCard = scheduler.reviewCard(card, command.rating()).card();
        flashcardStatisticRepository.save(command.flashcardId(), currentUser.userId(), updatedCard);
    }
}
