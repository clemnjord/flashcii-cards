package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckCommand;
import com.clemnjord.flashcii.application.port.output.IAuthorizationService;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UnauthorizedException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCardToDeckUseCaseImplTest {

    private final User testUser = User.createNew(new Username("testUser"));
    private final DeckId testDeckId = DeckId.generate();
    private final FlashcardId testFlashcardId = FlashcardId.generate();
    AddCardToDeckCommand command = new AddCardToDeckCommand(testDeckId, testFlashcardId, testUser.userId());
    Flashcard flashcard = Flashcard.restore(testFlashcardId, new Question("Question"), new Answer("Answer"));
    Deck deck = Deck.restore(testDeckId, "Test", "Desc", testUser.userId(), new HashSet<>());

    @Mock
    private IDeckRepository deckRepository;

    @Mock
    private IFlashcardRepository flashcardRepository;

    @Mock
    private IUserContextService userContextService;

    @Mock
    private IAuthorizationService authorizationService;

    @InjectMocks
    private AddCardToDeckUseCaseImpl addCardToDeckUseCase;

    @BeforeEach
    void setUp() {
        when(userContextService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void shouldAddFlashcardToDeck() {
        // Arrange
        mockAuthorization(true);
        mockFindByFlashcardAndOwnerId(true);
        mockFindDeckByIdAndOwnerId(true);

        // Act
        addCardToDeckUseCase.execute(command);

        // Assert
        verify(deckRepository).addFlashcardToDeck(testDeckId, testFlashcardId, testUser.userId());
    }

    @Test
    void shouldNotAddWhenFlashcardAlreadyInDeck() {
        // Arrange
        mockAuthorization(true);
        mockFindByFlashcardAndOwnerId(true);
        mockFindDeckByIdAndOwnerId(true);

        addCardToDeckUseCase.execute(command);
        deck = deck.addFlashcard(testFlashcardId);
        mockFindDeckByIdAndOwnerId(true);

        // Act
        addCardToDeckUseCase.execute(command);

        // Assert
        verify(deckRepository, times(1)).addFlashcardToDeck(testDeckId, testFlashcardId, testUser.userId());
    }

    @Test
    void shouldThrowWhenUnauthorized() {
        // Arrange
        mockAuthorization(false);

        // Act & Assert
        assertThatThrownBy(() -> addCardToDeckUseCase.execute(command))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining(
                        "User " + testUser.userId().uuid() + " is not authorized to add flashcards for user "
                                + command.ownerId().uuid());
    }

    @Test
    void shouldThrowWhenFlashcardNotFound() {
        // Arrange
        mockAuthorization(true);
        mockFindByFlashcardAndOwnerId(false);

        // Act & Assert
        assertThatThrownBy(() -> addCardToDeckUseCase.execute(command))
                .isInstanceOf(FlashcardNotFoundException.class)
                .hasMessageContaining(
                        "Flashcard not found with ID: " + command.flashcardId().uuid());
    }

    @Test
    void shouldThrowWhenDeckNotFound() {
        // Arrange
        mockAuthorization(true);
        mockFindByFlashcardAndOwnerId(true);
        mockFindDeckByIdAndOwnerId(false);

        // Act & Assert
        assertThatThrownBy(() -> addCardToDeckUseCase.execute(command))
                .isInstanceOf(DeckNotFoundException.class)
                .hasMessageContaining(
                        "Deck not found with ID: " + command.deckId().uuid());
    }

    // --- Helpers ---

    private void mockAuthorization(boolean canManage) {
        when(authorizationService.canManageResourceFor(any(), any())).thenReturn(canManage);
    }

    private void mockFindDeckByIdAndOwnerId(boolean isDeckFound) {
        var stub = when(deckRepository.findByIdAndOwnerId(testDeckId, testUser.userId()));

        if (isDeckFound) {
            stub.thenReturn(Optional.of(deck));
        } else {
            stub.thenReturn(Optional.empty());
        }
    }

    private void mockFindByFlashcardAndOwnerId(boolean isFlashcardFound) {
        var stub = when(flashcardRepository.findByFlashcardIdAndOwnerId(testFlashcardId, testUser.userId()));

        if (isFlashcardFound) {
            stub.thenReturn(Optional.of(flashcard));
        } else {
            stub.thenReturn(Optional.empty());
        }
    }
}