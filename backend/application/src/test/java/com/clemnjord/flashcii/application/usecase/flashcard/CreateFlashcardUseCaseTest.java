package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFlashcardUseCaseTest {

    private static final String DEFAULT_QUESTION = "What is Flashcii?";
    private static final String DEFAULT_ANSWER = "A flashcard app";
    private static final String DEFAULT_DECK_NAME = "Test Deck";
    private static final String DEFAULT_DECK_DESCRIPTION = "Test Description";
    private final User testUser = User.createNew(new Username("testUser"));
    private final DeckId testDeckId = new DeckId(UUID.randomUUID());
    @Mock
    private IDeckRepository deckRepository;
    @Mock
    private IFlashcardRepository flashcardRepository;
    @Mock
    private IUserContextService userContextService;
    @InjectMocks
    private CreateFlashcardUseCase createFlashcardUseCase;

    @BeforeEach
    void setUp() {
        createFlashcardUseCase = new CreateFlashcardUseCase(flashcardRepository, deckRepository, userContextService);

        when(userContextService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void shouldCreateFlashcardWhenDeckExists() {
        // Arrange
        var createCardCommand = createFlashcardCommand();

        // Mock the deck repository to return a deck when looking for the deck
        when(deckRepository.findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId())).thenReturn(Optional.of(createTestDeck()));

        // Act
        var result = createFlashcardUseCase.execute(createCardCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.question().value()).isEqualTo(DEFAULT_QUESTION);
        assertThat(result.answer().value()).isEqualTo(DEFAULT_ANSWER);

        verify(deckRepository).findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId());
        verify(userContextService).getCurrentUser();
    }

    @Test
    void shouldThrowExceptionWhenDeckDoesNotExist() {
        // Arrange
        var createCardCommand = createFlashcardCommand();

        // Mock the deck repository to return empty when looking for the deck
        when(deckRepository.findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> createFlashcardUseCase.execute(createCardCommand))
                .isInstanceOf(DeckNotFoundException.class)
                .hasMessageContaining("Deck not found with ID: " + createCardCommand.deckId().uuid());

        verify(deckRepository).findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId());
    }

    @Test
    void shouldThrowWhenFlashcardWithSameQuestionExistsInDeck() {
        // Arrange
        var createCardCommand = createFlashcardCommand();

        // Mock the deck exists
        when(deckRepository.findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId())).thenReturn(Optional.of(createTestDeck()));

        // Mock that flashcard with same question exists
        when(flashcardRepository.existsByQuestionAndDeckId(createCardCommand.question(), createCardCommand.deckId())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> createFlashcardUseCase.execute(createCardCommand))
                .isInstanceOf(FlashcardAlreadyExistsException.class)
                .hasMessageContaining("A flashcard with this question already exists in the deck");

        verify(deckRepository).findByIdAndOwnerId(createCardCommand.deckId(), testUser.userId());
        verify(flashcardRepository).existsByQuestionAndDeckId(createCardCommand.question(), createCardCommand.deckId());
    }

    // Helper methods for test data creation
    private CreateFlashcardCommand createFlashcardCommand(DeckId deckId) {
        return new CreateFlashcardCommand(deckId, new Question(DEFAULT_QUESTION), new Answer(DEFAULT_ANSWER));
    }

    private CreateFlashcardCommand createFlashcardCommand() {
        return createFlashcardCommand(testDeckId);
    }

    private Deck createTestDeck(DeckId deckId) {
        return Deck.restore(deckId, DEFAULT_DECK_NAME, DEFAULT_DECK_DESCRIPTION, testUser.userId(), new HashSet<>());
    }

    private Deck createTestDeck() {
        return createTestDeck(testDeckId);
    }
}
