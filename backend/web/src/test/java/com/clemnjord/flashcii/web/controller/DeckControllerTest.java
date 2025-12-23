package com.clemnjord.flashcii.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckUseCase;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.web.dto.DeckDto;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(DeckController.class)
class DeckControllerTest {

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

    @Nested
    class CreateDeckTests {
        @Test
        void shouldCreateDeck() {
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
                    .satisfies(response -> assertThat(response.name()).isEqualTo("New Deck"));
        }

        @Test
        void shouldReturn400WhenCreatingDeckWithInvalidInput() {
            DeckDto.DeckRequest invalidRequest = new DeckDto.DeckRequest("", "Valid description");

            mockMvc.post()
                    .uri("/decks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest))
                    .assertThat()
                    .hasStatus(HttpStatus.BAD_REQUEST)
                    .bodyJson()
                    .satisfies(response ->
                            response.assertThat().extractingPath("$.errorCode").isEqualTo("VALIDATION_FAILED"));
        }
    }

    @Nested
    class ListDecksTests {
        @Test
        void shouldReturnDecksWhenListing() {
            // Given
            Deck mockDeck = Deck.createNew("Test Deck", "Description", new UserId(UUID.randomUUID()));
            when(listDeckUseCase.execute(any())).thenReturn(List.of(mockDeck));

            // When
            mockMvc.get()
                    .uri("/decks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .assertThat()
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(InstanceOfAssertFactories.list(DeckDto.SimpleDeckResponse.class))
                    .hasSize(1)
                    .satisfies(result -> assertThat(result.getFirst().name()).isEqualTo("Test Deck"));
        }
    }

    @Nested
    class GetDeckByIdTests {
        @Test
        void shouldReturnDeckById() {
            // Given
            FlashcardId flashcardId = FlashcardId.generate();
            Deck mockDeck = Deck.createNew("Test Deck", "Description", new UserId(UUID.randomUUID()))
                    .addFlashcard(flashcardId);
            when(getDeckUseCase.execute(any())).thenReturn(mockDeck);

            // When
            mockMvc.get()
                    .uri("/decks/" + mockDeck.deckId().uuid().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .assertThat()
                    .hasStatus(HttpStatus.OK)
                    .bodyJson()
                    .convertTo(DeckDto.DeckResponse.class)
                    .satisfies(result -> {
                        assertThat(result.uuid())
                                .isEqualTo(mockDeck.deckId().uuid().toString());
                        assertThat(result.name()).isEqualTo("Test Deck");
                        assertThat(result.description()).isEqualTo("Description");
                        assertThat(result.flashcardIds()).hasSize(1);
                        assertThat(result.flashcardIds().getFirst())
                                .isEqualTo(flashcardId.uuid().toString());
                    });
        }

        @Test
        void getDeckById_shouldReturnStatus404_whenDeckNotFound() {
            // Given
            when(getDeckUseCase.execute(any())).thenThrow(new DeckNotFoundException("Deck not found"));

            // When
            mockMvc.get()
                    .uri("/decks/" + UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .assertThat()
                    .hasStatus(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    class AddFlashcardToDeckTests {
        DeckDto.AddFlashcardToDeckRequest request =
                new DeckDto.AddFlashcardToDeckRequest(UUID.randomUUID().toString());

        @Test
        void addFlashcardToDeck_shouldReturnStatus204_whenInputsValid() {
            // Given
            AddCardToDeckCommand command = new AddCardToDeckCommand(DeckId.generate(), FlashcardId.generate());
            doNothing().when(addCardToDeckUseCase).execute(command);

            // When & Then
            mockMvc.post()
                    .uri("/decks/" + UUID.randomUUID() + "/flashcards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .assertThat()
                    .hasStatus(HttpStatus.NO_CONTENT);
        }

        @Test
        void addFlashcardToDeck_shouldReturnStatus404_whenDeckNotFound() {
            // Given
            doThrow(new DeckNotFoundException("Deck not found"))
                    .when(addCardToDeckUseCase)
                    .execute(any());

            // When & Then
            mockMvc.post()
                    .uri("/decks/" + UUID.randomUUID() + "/flashcards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .assertThat()
                    .hasStatus(HttpStatus.NOT_FOUND);
        }

        @Test
        void addFlashcardToDeck_shouldReturnStatus404_whenFlashcardNotFound() {
            // Given
            doThrow(new FlashcardNotFoundException("Flashcard not found"))
                    .when(addCardToDeckUseCase)
                    .execute(any());

            // When & Then
            mockMvc.post()
                    .uri("/decks/" + UUID.randomUUID() + "/flashcards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .assertThat()
                    .hasStatus(HttpStatus.NOT_FOUND);
        }
    }
}
