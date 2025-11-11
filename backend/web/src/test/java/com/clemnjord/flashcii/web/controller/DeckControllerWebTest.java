package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.web.dto.DeckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(DeckController.class)
class DeckControllerWebTest {

    @Autowired
    private MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AddCardToDeckUseCase addCardToDeckUseCase;

    @MockitoBean
    private CreateDeckUseCase createDeckUseCase;

    @MockitoBean
    private GetDeckUseCase getDeckUseCase;

    @MockitoBean
    private ListDeckUseCase listDeckUseCase;

    @Test
    void shouldCreateDeck() throws Exception {
        // Given
        DeckDto.DeckRequest request = new DeckDto.DeckRequest("New Deck", "New Description");
        Deck mockDeck = Deck.createNew("New Deck", "New Description", new UserId(UUID.randomUUID()));
        when(createDeckUseCase.execute(any())).thenReturn(mockDeck);

        // When & Then
        mockMvc.post()
                .uri("/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .assertThat()
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(DeckDto.DeckResponse.class)
                .satisfies(response -> {
                    assertThat(response.name()).isEqualTo("New Deck");
                });
    }

    @Test
    void shouldReturn400WhenCreatingDeckWithInvalidInput() throws Exception {
        DeckDto.DeckRequest invalidRequest = new DeckDto.DeckRequest("", "Valid description");

        mockMvc.post()
                .uri("/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .assertThat()
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .satisfies(response -> {
                    response.assertThat().extractingPath("$.errorCode").isEqualTo("VALIDATION_FAILED");
                });
    }
}
