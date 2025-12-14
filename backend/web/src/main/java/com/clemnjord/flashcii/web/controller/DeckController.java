package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.web.dto.DeckDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/decks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Decks", description = "Deck management operations")
public class DeckController {

    private final AddCardToDeckUseCase addCardToDeckUseCase;
    private final CreateDeckUseCase createDeckUseCase;
    private final GetDeckUseCase getDeckUseCase;
    private final ListDeckUseCase listDeckUseCase;

    @GetMapping
    @Operation(summary = "List decks", description = "Retrieve all decks with optional name filtering")
    @ApiResponse(responseCode = "200", description = "Decks retrieved successfully")
    public List<DeckDto.SimpleDeckResponse> getDecks(
            @Parameter(description = "Filter decks with optional name filter (case-insensitive")
                    @RequestParam(required = false)
                    String nameFilter) {
        return listDeckUseCase.execute(new ListDeckCommand(nameFilter)).stream()
                .map(deck -> new DeckDto.SimpleDeckResponse(
                        deck.deckId().uuid().toString(), deck.name(), deck.description()))
                .toList();
    }

    @PostMapping
    @Operation(summary = "Create deck", description = "Create a new deck")
    @ApiResponse(responseCode = "201", description = "Deck created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckDto.DeckResponse createDeck(@Valid @RequestBody DeckDto.DeckRequest deckRequest) {
        Deck deck = createDeckUseCase.execute(
                new CreateDeckCommand(deckRequest.name(), deckRequest.description(), List.of()));
        return new DeckDto.DeckResponse(
                deck.deckId().uuid().toString(),
                deck.name(),
                deck.description(),
                deck.flashcardIds().stream().map(f -> f.uuid().toString()).toList());
    }

    @GetMapping("/{deckId}")
    @Operation(summary = "Get deck", description = "Retrieve a deck by its ID")
    @ApiResponse(responseCode = "200", description = "Deck retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Deck not owned")
    @ApiResponse(responseCode = "404", description = "Deck not found")
    public DeckDto.DeckResponse getDeck(
            @Parameter(description = "UUID of the deck to retrieve") @PathVariable String deckId) {
        Deck deck = getDeckUseCase.execute(new GetDeckCommand(DeckId.from(deckId)));
        return new DeckDto.DeckResponse(
                deck.deckId().uuid().toString(),
                deck.name(),
                deck.description(),
                deck.flashcardIds().stream().map(f -> f.uuid().toString()).toList());
    }

    @PostMapping("/{deckId}/flashcards")
    @Operation(summary = "Add flashcard to deck", description = "Add an existing flashcard to a deck")
    @ApiResponse(responseCode = "204", description = "Flashcard added successfully")
    @ApiResponse(responseCode = "403", description = "User not authorized")
    @ApiResponse(responseCode = "404", description = "Deck or flashcard not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFlashcardToDeck(
            @Parameter(description = "UUID of the deck") @PathVariable String deckId,
            @Parameter(description = "UUID of the flashcard") @Valid @RequestBody
                    DeckDto.AddFlashcardToDeckRequest request) {
        addCardToDeckUseCase.execute(
                new AddCardToDeckCommand(DeckId.from(deckId), FlashcardId.from(request.flashcardId())));
    }
}
