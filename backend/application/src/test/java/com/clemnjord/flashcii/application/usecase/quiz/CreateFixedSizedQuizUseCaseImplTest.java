package com.clemnjord.flashcii.application.usecase.quiz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizCommand;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFixedSizedQuizRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateFixedSizedQuizUseCaseImplTest {
    @InjectMocks
    CreateFixedSizedQuizUseCaseImpl createFixedSizeQuizUseCase;

    @Mock
    IUserContextService userContextService;

    @Mock
    IDeckRepository deckRepository;

    @Mock
    IFlashcardRepository flashcardRepository;

    @Mock
    IFixedSizedQuizRepository fixedSizeQuizRepository;

    private final User defaultUser = User.createNew(new Username("testUser"));
    private final DeckId deckId1 = DeckId.generate();
    private final DeckId deckId2 = DeckId.generate();
    private final DeckId deckId3 = DeckId.generate();

    @Test
    @DisplayName("Creating FixedSizedQuiz with nominal inputs should succeed")
    void creatingCreateFixedSizedQuiz_shouldSucceed_whenValidInputs() {
        // --- Given
        when(userContextService.getCurrentUser()).thenReturn(defaultUser);
        doNothing().when(fixedSizeQuizRepository).save(any(), any());

        Flashcard flashcard1 = Flashcard.createNew(new Question("Question 1"), new Answer("Answer 1"));
        Flashcard flashcard2 = Flashcard.createNew(new Question("Question 2"), new Answer("Answer 2"));
        Flashcard flashcard3 = Flashcard.createNew(new Question("Question 3"), new Answer("Answer 3"));

        Set<FlashcardId> flashcardIdsDeck1 = Set.of(flashcard1.flashcardId(), flashcard2.flashcardId());
        Set<FlashcardId> flashcardIdsDeck2 = Set.of(flashcard2.flashcardId(), flashcard3.flashcardId());

        // Decks 1 and 2 exist and are owned by this user
        when(deckRepository.findByIdAndOwnerId(deckId1, defaultUser.userId()))
                .thenReturn(Optional.of(
                        new Deck(deckId1, "testDeck1", "description deck 1", defaultUser.userId(), flashcardIdsDeck1)));
        when(deckRepository.findByIdAndOwnerId(deckId2, defaultUser.userId()))
                .thenReturn(Optional.of(
                        new Deck(deckId2, "testDeck2", "description deck 2", defaultUser.userId(), flashcardIdsDeck2)));
        // Deck 3 does not exist or is not owner by this user
        when(deckRepository.findByIdAndOwnerId(deckId3, defaultUser.userId())).thenReturn(Optional.empty());

        when(flashcardRepository.findDueFlashcardsByDeckIdsAndOwnerId(Set.of(deckId1, deckId2), defaultUser.userId()))
                .thenReturn(Set.of(flashcard1, flashcard2, flashcard3));

        CreateFixedSizedQuizCommand command = new CreateFixedSizedQuizCommand(Set.of(deckId1, deckId2, deckId3));

        // --- When
        FixedSizedQuiz quiz = createFixedSizeQuizUseCase.execute(command);

        // --- Then
        assertThat(quiz).isNotNull();
        assertThat(quiz.getId()).isNotNull();
        assertThat(quiz.isFinished()).isFalse();
    }

    @Test
    @DisplayName("Creating FixedSizedQuiz without nominal inputs should succeed")
    void creatingCreateFixedSizedQuiz_shouldThrow_whenNoDueFlashcards() {
        // --- Given
        when(userContextService.getCurrentUser()).thenReturn(defaultUser);

        when(flashcardRepository.findDueFlashcardsByDeckIdsAndOwnerId(Set.of(), defaultUser.userId()))
                .thenReturn(Set.of());

        CreateFixedSizedQuizCommand command = new CreateFixedSizedQuizCommand(Set.of());

        // --- When & Then
        assertThatThrownBy(() -> createFixedSizeQuizUseCase.execute(command)).isInstanceOf(IllegalStateException.class);
    }
}
