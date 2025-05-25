package com.clemnjord.flashcii.service.flashcard;

import com.clemnjord.flashcii.domain.flashcard.model.Collection;
import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;
import com.clemnjord.flashcii.domain.flashcard.repository.ICollectionRepository;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.model.UserId;
import com.clemnjord.flashcii.service.flashcard.command.CreateCollectionCommand;
import com.clemnjord.flashcii.service.flashcard.usecase.CreateCollectionUseCase;
import com.clemnjord.flashcii.service.user.usecase.ICurrentUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreateCollectionUseCaseTest {

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

        var createCollectionCommand = new CreateCollectionCommand("testCollection",
                "Test description", Collections.emptyList());


        // Act
        var result = createCollectionUseCase.execute(createCollectionCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("testCollection");
        assertThat(result.description()).isEqualTo("Test description");
        assertThat(result.ownerId()).isEqualTo(userId);
        assertThat(result.cards()).isEmpty();
    }

    private void mockGetCurrentUser(UserId userId) {
        when(currentUserUseCase.getCurrentUser()).thenReturn(new User(userId, "testUsername"));
    }

    private void mockCollectionRepositorySave() {
        when(collectionRepository.save(any())).thenAnswer(invocation -> {
            var collection = (Collection) invocation.getArgument(0);

            var collectionId = new CollectionId(UUID.randomUUID());
            return new Collection(collectionId, collection.name(), collection.description(), collection.ownerId(), collection.cards());
        });
    }

    private void mockCollectionDoesNotExist() {
        when(collectionRepository.existsByName(any())).thenReturn(false);
    }

    private void collectionExists() {
        when(collectionRepository.existsByName(any())).thenReturn(true);
    }
}
