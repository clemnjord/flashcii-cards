package com.clemnjord.flashcii.service.flashcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.flashcard.command.CreateCardCommand;
import com.clemnjord.flashcii.domain.flashcard.exception.CollectionNotFoundException;
import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;
import com.clemnjord.flashcii.domain.flashcard.repository.ICardRepository;
import com.clemnjord.flashcii.domain.flashcard.repository.ICollectionRepository;
import com.clemnjord.flashcii.domain.user.model.UserId;
import com.clemnjord.flashcii.service.flashcard.usecase.CreateCardUseCase;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCardUseCaseTest {

  ICollectionRepository collectionRepository;
  ICardRepository cardRepository;
  CreateCardUseCase createCardUseCase;

  @BeforeEach
  void setUp() {
    collectionRepository = mock(ICollectionRepository.class);
    cardRepository = mock(ICardRepository.class);
    createCardUseCase = new CreateCardUseCase(cardRepository, collectionRepository);
  }

  @Test
  void shouldCreateCardWhenCollectionExists() {
    // Arrange
    CollectionId collectionId = new CollectionId(UUID.randomUUID());
    var createCardCommand =
        new CreateCardCommand(collectionId, "What is Flashcii?", "A flashcard app");

    // Mock the collection repository to return a collection when looking for the collection
    when(collectionRepository.findById(createCardCommand.collectionId()))
        .thenReturn(
            Optional.of(
                new com.clemnjord.flashcii.domain.flashcard.model.Collection(
                    collectionId,
                    "Test Collection",
                    "Test Description",
                    new UserId(UUID.randomUUID()),
                    new ArrayList<>())));

    // Mock the card repository to return a saved card
    when(cardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    var result = createCardUseCase.execute(createCardCommand);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.question()).isEqualTo("What is Flashcii?");
    assertThat(result.answer()).isEqualTo("A flashcard app");
  }

  @Test
  void shouldThrowIfCollectionDoesNotExist() {
    // Arrange
    CollectionId collectionId = new CollectionId(UUID.randomUUID());
    var createCardCommand =
        new CreateCardCommand(collectionId, "What is Flashcii?", "A flashcard app");

    // Mock the collection repository to return empty when looking for the collection
    when(collectionRepository.findById(createCardCommand.collectionId()))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> createCardUseCase.execute(createCardCommand))
        .isInstanceOf(CollectionNotFoundException.class)
        .hasMessageContaining("Collection not found with ID: " + collectionId.uuid());
  }
}
