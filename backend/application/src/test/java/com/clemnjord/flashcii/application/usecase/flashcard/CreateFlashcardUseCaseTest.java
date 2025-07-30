package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.port.input.flashcard.ICreateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateFlashcardUseCaseTest {

  IDeckRepository deckRepository;
  IFlashcardRepository flashcardRepository;
  CreateFlashcardUseCase createFlashcardUseCase;

  @BeforeEach
  void setUp() {
    deckRepository = mock(IDeckRepository.class);
    flashcardRepository = mock(IFlashcardRepository.class);
    createFlashcardUseCase = new CreateFlashcardUseCase(flashcardRepository, deckRepository);
  }

  @Test
  void shouldCreateCardWhenDeckExists() {
    // Arrange
    DeckId deckId = new DeckId(UUID.randomUUID());
    var createCardCommand =
            new ICreateFlashcardUseCase.CreateFlashcardCommand(
                    deckId, "What is Flashcii?", "A flashcard app");

    // Mock the deck repository to return a deck when looking for the deck
    when(deckRepository.findById(createCardCommand.deckId()))
        .thenReturn(
            Optional.of(
                    new Deck(
                            deckId,
                            "Test Deck",
                    "Test Description",
                    new UserId(UUID.randomUUID()),
                            new HashSet<>())));

    // Mock the flashcard repository to return a saved flashcard
    when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    var result = createFlashcardUseCase.execute(createCardCommand);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.question().value()).isEqualTo("What is Flashcii?");
    assertThat(result.answer().value()).isEqualTo("A flashcard app");
  }

  @Test
  void shouldThrowIfDeckDoesNotExist() {
    // Arrange
    DeckId deckId = new DeckId(UUID.randomUUID());
    var createCardCommand =
            new ICreateFlashcardUseCase.CreateFlashcardCommand(
                    deckId, "What is Flashcii?", "A flashcard app");

    // Mock the deck repository to return empty when looking for the deck
    when(deckRepository.findById(createCardCommand.deckId()))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> createFlashcardUseCase.execute(createCardCommand))
            .isInstanceOf(DeckNotFoundException.class)
            .hasMessageContaining("Deck not found with ID: " + deckId.uuid());
  }

  @Test
  void shouldThrowWhenFlashcardWithSameQuestionExistsInDeck() {
    // Arrange
    DeckId deckId = new DeckId(UUID.randomUUID());
    var createCardCommand =
            new ICreateFlashcardUseCase.CreateFlashcardCommand(
                    deckId, "What is Flashcii?", "A flashcard app");

    // Mock the deck exists
    when(deckRepository.findById(createCardCommand.deckId()))
        .thenReturn(
            Optional.of(
                    new Deck(
                            deckId,
                            "Test Deck",
                    "Test Description",
                    new UserId(UUID.randomUUID()),
                            new HashSet<>())));

    // Mock that flashcard with same question exists
    when(flashcardRepository.existsByQuestionAndDeckId(
            new Question(createCardCommand.question()), createCardCommand.deckId()))
        .thenReturn(true);

    // Act & Assert
    assertThatThrownBy(() -> createFlashcardUseCase.execute(createCardCommand))
            .isInstanceOf(FlashcardAlreadyExistsException.class)
            .hasMessageContaining("A flashcard with this question already exists in the deck");
  }
}
