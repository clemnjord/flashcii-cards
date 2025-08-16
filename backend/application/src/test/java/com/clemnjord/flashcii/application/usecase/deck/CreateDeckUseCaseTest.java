package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.port.input.deck.CreateDeckCommand;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateDeckUseCaseTest {
    @InjectMocks
    CreateDeckUseCase createDeckUseCase;
    @Mock
    private IDeckRepository deckRepository;
    @Mock
    private IUserContextService userContextService;

    @Test
    void shouldCreateDeckWhenNameIsUnique() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        mockGetCurrentUser(userId);
        mockDeckDoesNotExist();

        var createDeckCommand = new CreateDeckCommand("testDeck", "Test description", Collections.emptyList());

        // Act
        var result = createDeckUseCase.execute(createDeckCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("testDeck");
        assertThat(result.description()).isEqualTo("Test description");
        assertThat(result.ownerId()).isEqualTo(userId);
        assertThat(result.flashcardIds()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeckNameAlreadyExists() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        mockGetCurrentUser(userId);
        deckExists();

        var createDeckCommand = new CreateDeckCommand("testDeck", "Test description", Collections.emptyList());

        // Act & Assert
        assertThatThrownBy(() -> createDeckUseCase.execute(createDeckCommand))
                .isInstanceOf(DeckAlreadyExistsException.class)
                .hasMessageContaining("Deck with name 'testDeck' already exists");
    }

    @Test
    void shouldCreateDeckWithTags() {
        // Test deck creation with tags
        UserId userId = new UserId(UUID.randomUUID());
        mockGetCurrentUser(userId);
        mockDeckDoesNotExist();

        var createDeckCommand = new CreateDeckCommand("testDeck", "Test description", List.of("tag1", "tag2"));

        var result = createDeckUseCase.execute(createDeckCommand);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("testDeck");
    }

    // Helper test methods
    private void mockGetCurrentUser(UserId userId) {
        when(userContextService.getCurrentUser()).thenReturn(new User(userId, new Username("testUsername")));
    }

    private void mockDeckDoesNotExist() {
        when(deckRepository.existsByNameAndOwnerId(any(), any())).thenReturn(false);
    }

    private void deckExists() {
        when(deckRepository.existsByNameAndOwnerId(any(), any())).thenReturn(true);
    }
}
