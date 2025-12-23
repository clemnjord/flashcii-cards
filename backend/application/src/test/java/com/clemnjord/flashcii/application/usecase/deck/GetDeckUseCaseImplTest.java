package com.clemnjord.flashcii.application.usecase.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.deck.GetDeckCommand;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetDeckUseCaseImplTest {
    @Mock
    private IDeckRepository deckRepository;

    @Mock
    private IUserContextService userContextService;

    @InjectMocks
    private GetDeckUseCaseImpl getDeckUseCaseImpl;

    User defaultUser = new User(UserId.generate(), new Username("defaultUser"));

    @Test
    void creatingCommand_shouldSucceed_whenInputValid() {
        // --- Given
        DeckId deckId = DeckId.generate();

        // --- When
        GetDeckCommand command = new GetDeckCommand(deckId);

        // --- Then
        assertThat(command.deckId()).isEqualTo(deckId);
    }

    @Test
    void creatingCommand_shouldThrow_whenInputIsNull() {
        // --- Given & When & Then
        assertThatThrownBy(() -> new GetDeckCommand(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getDeckById_shouldReturnDeck_whenDeckExists() {
        // --- Given
        Deck expectedDeck = Deck.createNew("deckName", "deckDescription", defaultUser.userId());

        var command = new GetDeckCommand(expectedDeck.deckId());
        when(deckRepository.findByIdAndOwnerId(command.deckId(), defaultUser.userId()))
                .thenReturn(Optional.of(expectedDeck));

        when(userContextService.getCurrentUser()).thenReturn(defaultUser);

        // --- When
        Deck result = getDeckUseCaseImpl.execute(command);

        // Assert
        assertThat(result).isEqualTo(expectedDeck);
    }

    @Test
    void getDeckById_shouldThrow_whenDeckDoesNotExistOrNotOwnedByCurrentUser() {
        // --- Given
        when(userContextService.getCurrentUser()).thenReturn(defaultUser);
        when(deckRepository.findByIdAndOwnerId(any(), any())).thenReturn(Optional.empty());

        var command = new GetDeckCommand(DeckId.generate());

        // --- When & Then
        assertThatThrownBy(() -> getDeckUseCaseImpl.execute(command)).isInstanceOf(DeckNotFoundException.class);
    }
}
