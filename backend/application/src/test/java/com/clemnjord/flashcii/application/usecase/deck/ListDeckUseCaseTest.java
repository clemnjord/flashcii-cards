package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.port.input.deck.IListDeckUseCase;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckFactory;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ListDeckUseCaseTest {
    @Mock
    private IDeckRepository deckRepository;
    @Mock
    private ICurrentUserUseCase currentUserUseCase;
    private ListDeckUseCase listDeckUseCase;
    private UserId userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listDeckUseCase = new ListDeckUseCase(deckRepository, currentUserUseCase);
        userId = UserId.generate();
    }

    @Test
    void shouldCreateCommand() {
        // Act
        IListDeckUseCase.ListDeckCommand command = new IListDeckUseCase.ListDeckCommand("nameFilter");

        // Assert
        assertThat(command.nameFilter()).isEqualTo("nameFilter");
    }

    @Test
    void shouldReturnDecksWithoutFilter() {
        // Arrange
        List<Deck> expectedDecks = List.of(DeckFactory.createEmpty("deckName", "deckDescription", userId));

        var command = new IListDeckUseCase.ListDeckCommand(null);
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, command.nameFilter())).thenReturn(expectedDecks);

        when(currentUserUseCase.getCurrentUser()).thenReturn(new User(userId, new Username("testUser")));

        // Act
        List<Deck> result = listDeckUseCase.execute(command);

        // Assert
        assertThat(result).isEqualTo(expectedDecks);
    }

    @Test
    void shouldReturnDecksWithNameFilter() {
        // Test filtering functionality
        List<Deck> expectedDecks = List.of(DeckFactory.createEmpty("filteredDeck", "description", userId));

        var command = new IListDeckUseCase.ListDeckCommand("filtered");
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, "filtered"))
                .thenReturn(expectedDecks);
        when(currentUserUseCase.getCurrentUser())
                .thenReturn(new User(userId, new Username("testUser")));

        List<Deck> result = listDeckUseCase.execute(command);

        assertThat(result).isEqualTo(expectedDecks);
    }

    @Test
    void shouldReturnEmptyListWhenNoDecksFound() {
        // Test empty result case
        var command = new IListDeckUseCase.ListDeckCommand("nonexistent");
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, "nonexistent"))
                .thenReturn(List.of());
        when(currentUserUseCase.getCurrentUser())
                .thenReturn(new User(userId, new Username("testUser")));

        List<Deck> result = listDeckUseCase.execute(command);

        assertThat(result).isEmpty();
    }
}
