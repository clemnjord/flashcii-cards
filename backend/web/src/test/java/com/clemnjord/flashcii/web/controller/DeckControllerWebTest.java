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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeckController.class)
class DeckControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(post("/decks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Deck"));
    }

    @Test
    void shouldReturn400WhenCreatingDeckWithInvalidInput() throws Exception {
        DeckDto.DeckRequest invalidRequest = new DeckDto.DeckRequest("", "Valid description");

        mockMvc.perform(post("/decks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }
}