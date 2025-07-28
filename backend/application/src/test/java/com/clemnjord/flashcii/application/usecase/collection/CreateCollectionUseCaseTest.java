package com.clemnjord.flashcii.application.usecase.collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.output.ICollectionRepository;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.domain.exception.collection.CollectionAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.*;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateCollectionUseCaseTest {

  ICollectionRepository collectionRepository;
  ICurrentUserUseCase currentUserUseCase;
  CreateCollectionUseCase createCollectionUseCase;

  @BeforeEach
  void setUp() {
    collectionRepository = Mockito.mock(ICollectionRepository.class);
    currentUserUseCase = Mockito.mock(ICurrentUserUseCase.class);
    createCollectionUseCase = new CreateCollectionUseCase(collectionRepository, currentUserUseCase);
  }

  @Test
  void shouldCreateCollectionWhenNameIsUnique() {
    // Arrange
    UserId userId = new UserId(UUID.randomUUID());
    mockGetCurrentUser(userId);
    mockCollectionRepositorySave();
    mockCollectionDoesNotExist();

    var createCollectionCommand =
        new CreateCollectionUseCase.CreateCollectionCommand(
            "testCollection", "Test description", Collections.emptyList());

    // Act
    var result = createCollectionUseCase.execute(createCollectionCommand);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("testCollection");
    assertThat(result.description()).isEqualTo("Test description");
    assertThat(result.ownerId()).isEqualTo(userId);
    assertThat(result.cards()).isEmpty();
  }

  @Test
  void shouldThrowExceptionWhenCollectionNameAlreadyExists() {
    // Arrange
    UserId userId = new UserId(UUID.randomUUID());
    mockGetCurrentUser(userId);
    collectionExists();

    var createCollectionCommand =
        new CreateCollectionUseCase.CreateCollectionCommand(
            "testCollection", "Test description", Collections.emptyList());

    // Act & Assert
    assertThatThrownBy(() -> createCollectionUseCase.execute(createCollectionCommand))
        .isInstanceOf(CollectionAlreadyExistsException.class)
        .hasMessageContaining("Collection with name 'testCollection' already exists");
  }

  private void mockGetCurrentUser(UserId userId) {
    when(currentUserUseCase.getCurrentUser())
        .thenReturn(new User(userId, new Username("testUsername")));
  }

  private void mockCollectionRepositorySave() {
    when(collectionRepository.save(any()))
        .thenAnswer(
            invocation -> {
              var collection = (Collection) invocation.getArgument(0);

              var collectionId = new CollectionId(UUID.randomUUID());
              return new Collection(
                  collectionId,
                  collection.name(),
                  collection.description(),
                  collection.ownerId(),
                  collection.cards());
            });
  }

  private void mockCollectionDoesNotExist() {
    when(collectionRepository.existsByName(any())).thenReturn(false);
  }

  private void collectionExists() {
    when(collectionRepository.existsByName(any())).thenReturn(true);
  }
}
