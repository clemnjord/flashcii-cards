package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.port.input.deck.ICreateDeckUseCase;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateDeckUseCaseTest {
    @Mock
    IDeckRepository deckRepository;
    @Mock
    ICurrentUserUseCase currentUserUseCase;
    CreateDeckUseCase createDeckUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        deckRepository = Mockito.mock(IDeckRepository.class);
        currentUserUseCase = Mockito.mock(ICurrentUserUseCase.class);
        createDeckUseCase = new CreateDeckUseCase(deckRepository, currentUserUseCase);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void shouldCreateDeckWhenNameIsUnique() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        mockGetCurrentUser(userId);
        mockDeckRepositorySave();
        mockDeckDoesNotExist();

        var createDeckCommand =
                new ICreateDeckUseCase.CreateDeckCommand(
                        "testDeck", "Test description", Collections.emptyList());

        // Act
        var result = createDeckUseCase.execute(createDeckCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("testDeck");
        assertThat(result.getDescription()).isEqualTo("Test description");
        assertThat(result.getOwnerId()).isEqualTo(userId);
        assertThat(result.getFlashcardIds()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeckNameAlreadyExists() {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        mockGetCurrentUser(userId);
        deckExists();

        var createDeckCommand =
                new ICreateDeckUseCase.CreateDeckCommand(
                        "testDeck", "Test description", Collections.emptyList());

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
        mockDeckRepositorySave();
        mockDeckDoesNotExist();

        var createDeckCommand = new ICreateDeckUseCase.CreateDeckCommand(
                "testDeck", "Test description", List.of("tag1", "tag2"));

        var result = createDeckUseCase.execute(createDeckCommand);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("testDeck");
    }

    private void mockGetCurrentUser(UserId userId) {
        when(currentUserUseCase.getCurrentUser())
                .thenReturn(new User(userId, new Username("testUsername")));
    }

    private void mockDeckRepositorySave() {
        when(deckRepository.save(any()))
                .thenAnswer(
                        invocation -> {
                            var deck = (Deck) invocation.getArgument(0);

                            var deckId = new DeckId(UUID.randomUUID());
                            return new Deck(
                                    deckId,
                                    deck.getName(),
                                    deck.getDescription(),
                                    deck.getOwnerId(),
                                    deck.getFlashcardIds());
                        });
    }

    private void mockDeckDoesNotExist() {
        when(deckRepository.existsByName(any())).thenReturn(false);
    }

    private void deckExists() {
        when(deckRepository.existsByName(any())).thenReturn(true);
    }
}
