package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.web.dto.DeckDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckControllerTest {

    @Mock
    private ListDeckUseCase listDeckUseCase;

    @Mock
    private CreateDeckUseCase createDeckUseCase;

    @InjectMocks
    private DeckController deckController;

    @Test
    void shouldReturnDecksWhenListing() {
        // Given
        Deck mockDeck = Deck.createNew("Test Deck", "Description", new UserId(UUID.randomUUID()));
        when(listDeckUseCase.execute(any())).thenReturn(List.of(mockDeck));

        // When
        List<DeckDto.DeckResponse> result = deckController.getDecks("test");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Test Deck");
    }
}