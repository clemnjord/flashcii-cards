package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.port.input.deck.ListDeckCommand;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListDeckUseCaseTest {
    @Mock
    private IDeckRepository deckRepository;

    @Mock
    private IUserContextService userContextService;

    @InjectMocks
    private ListDeckUseCase listDeckUseCase;

    private UserId userId;

    @BeforeEach
    void setUp() {
        userId = UserId.generate();
    }

    @Test
    void shouldCreateCommand() {
        // Act
        ListDeckCommand command = new ListDeckCommand("nameFilter");

        // Assert
        assertThat(command.nameFilter()).isEqualTo("nameFilter");
    }

    @Test
    void shouldReturnDecksWithoutFilter() {
        // Arrange
        List<Deck> expectedDecks = List.of(Deck.createNew("deckName", "deckDescription", userId));

        var command = new ListDeckCommand(null);
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, command.nameFilter())).thenReturn(expectedDecks);

        when(userContextService.getCurrentUser()).thenReturn(new User(userId, new Username("testUser")));

        // Act
        List<Deck> result = listDeckUseCase.execute(command);

        // Assert
        assertThat(result).isEqualTo(expectedDecks);
    }

    @Test
    void shouldReturnDecksWithNameFilter() {
        // Test filtering functionality
        List<Deck> expectedDecks = List.of(Deck.createNew("filteredDeck", "description", userId));

        var command = new ListDeckCommand("filtered");
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, "filtered")).thenReturn(expectedDecks);
        when(userContextService.getCurrentUser()).thenReturn(new User(userId, new Username("testUser")));

        List<Deck> result = listDeckUseCase.execute(command);

        assertThat(result).isEqualTo(expectedDecks);
    }

    @Test
    void shouldReturnEmptyListWhenNoDecksFound() {
        // Test empty result case
        var command = new ListDeckCommand("nonexistent");
        when(deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(userId, "nonexistent")).thenReturn(List.of());
        when(userContextService.getCurrentUser()).thenReturn(new User(userId, new Username("testUser")));

        List<Deck> result = listDeckUseCase.execute(command);

        assertThat(result).isEmpty();
    }
}
