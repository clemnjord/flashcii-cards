package com.clemnjord.flashcii.application.usecase.flashcard;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardCommand;
import com.clemnjord.flashcii.application.port.output.IFixedSizedQuizRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardStatisticRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.Rating;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateFlashcardUseCaseImplTest {
    @InjectMocks
    RateFlashcardUseCaseImpl rateFlashcardUseCase;

    @Mock
    private IUserContextService userContextService;

    @Mock
    private IFlashcardRepository flashcardRepository;

    @Mock
    private IFlashcardStatisticRepository flashcardStatisticRepository;

    @Mock
    private IFixedSizedQuizRepository quizRepository;

    RateFlashcardCommand command;
    User defaultUser;
    Flashcard flashcard;
    Card existingCard;
    FixedSizedQuiz quiz;

    @BeforeEach
    void setUp() {
        defaultUser = User.createNew(new Username("testUser"));
        flashcard = Flashcard.createNew(new Question("Question"), new Answer("Answer"));
        quiz = FixedSizedQuiz.createNew(List.of(flashcard.flashcardId()));
        command = new RateFlashcardCommand(quiz.getId(), flashcard.flashcardId(), Rating.GOOD);
        existingCard = Card.builder().build();

        when(userContextService.getCurrentUser()).thenReturn(defaultUser);
    }

    @Test
    void shouldRateFlashcard_whenValidInput_firstRating() {
        when(quizRepository.findById(command.quizId())).thenReturn(Optional.of(quiz));
        when(flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), defaultUser.userId()))
                .thenReturn(Optional.of(flashcard));
        when(flashcardStatisticRepository.get(flashcard.flashcardId(), defaultUser.userId()))
                .thenReturn(Optional.empty());

        rateFlashcardUseCase.execute(command);

        verify(flashcardStatisticRepository)
                .save(eq(flashcard.flashcardId()), eq(defaultUser.userId()), any(Card.class));
    }

    @Test
    void shouldRateFlashcard_whenValidInput_newRating() {
        when(quizRepository.findById(command.quizId())).thenReturn(Optional.of(quiz));
        when(flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), defaultUser.userId()))
                .thenReturn(Optional.of(flashcard));
        when(flashcardStatisticRepository.get(flashcard.flashcardId(), defaultUser.userId()))
                .thenReturn(Optional.of(existingCard));

        rateFlashcardUseCase.execute(command);

        verify(flashcardStatisticRepository)
                .save(
                        eq(flashcard.flashcardId()),
                        eq(defaultUser.userId()),
                        argThat(card -> card != existingCard) // Card has been updated
                        );
    }

    @Test
    void shouldThrow_whenUserDoesntOwnFlashcard() {
        when(quizRepository.findById(command.quizId())).thenReturn(Optional.of(quiz));
        when(flashcardRepository.findByFlashcardIdAndOwnerId(command.flashcardId(), defaultUser.userId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rateFlashcardUseCase.execute(command)).isInstanceOf(FlashcardNotFoundException.class);
    }

    // TODO: Additional tests can be added here for other scenarios like QuizNotFoundException
}
