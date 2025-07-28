package com.clemnjord.flashcii.application.usecase.card;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import com.clemnjord.flashcii.domain.exception.card.CardAlreadyExistsException;
import com.clemnjord.flashcii.domain.exception.collection.CollectionNotFoundException;
import com.clemnjord.flashcii.domain.model.Collection;
import com.clemnjord.flashcii.domain.model.CollectionId;
import com.clemnjord.flashcii.domain.model.UserId;
import com.clemnjord.flashcii.application.port.output.ICardRepository;
import com.clemnjord.flashcii.application.port.output.ICollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateCardUseCaseTest {

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
                new CreateCardUseCase.CreateCardCommand(collectionId, "What is Flashcii?", "A flashcard app");

        // Mock the collection repository to return a collection when looking for the collection
        when(collectionRepository.findById(createCardCommand.collectionId()))
                .thenReturn(
                        Optional.of(
                                new Collection(
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
        assertThat(result.question().value()).isEqualTo("What is Flashcii?");
        assertThat(result.answer().value()).isEqualTo("A flashcard app");
    }

    @Test
    void shouldThrowIfCollectionDoesNotExist() {
        // Arrange
        CollectionId collectionId = new CollectionId(UUID.randomUUID());
        var createCardCommand =
                new CreateCardUseCase.CreateCardCommand(collectionId, "What is Flashcii?", "A flashcard app");

        // Mock the collection repository to return empty when looking for the collection
        when(collectionRepository.findById(createCardCommand.collectionId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> createCardUseCase.execute(createCardCommand))
                .isInstanceOf(CollectionNotFoundException.class)
                .hasMessageContaining("Collection not found with ID: " + collectionId.uuid());
    }

    @Test
    void shouldThrowWhenCardWithSameQuestionExistsInCollection() {
        // Arrange
        CollectionId collectionId = new CollectionId(UUID.randomUUID());
        var createCardCommand =
                new CreateCardUseCase.CreateCardCommand(collectionId, "What is Flashcii?", "A flashcard app");

        // Mock the collection exists
        when(collectionRepository.findById(createCardCommand.collectionId()))
                .thenReturn(Optional.of(
                        new Collection(
                                collectionId,
                                "Test Collection",
                                "Test Description",
                                new UserId(UUID.randomUUID()),
                                new ArrayList<>())));

        // Mock that card with same question exists
        when(cardRepository.existsByQuestionAndCollectionId(
                createCardCommand.question(), createCardCommand.collectionId()))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> createCardUseCase.execute(createCardCommand))
                .isInstanceOf(CardAlreadyExistsException.class)
                .hasMessageContaining("A card with this question already exists in the collection");
    }

}
