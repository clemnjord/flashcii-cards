package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.*;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.web.dto.DeckDto;
import com.clemnjord.flashcii.web.dto.FlashcardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/decks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Decks", description = "Deck management operations")
public class DeckController {

    private final GetDeckUseCase getDeckUseCase;
    private final ListDeckUseCase listDeckUseCase;
    private final CreateDeckUseCase createDeckUseCase;
    private final CreateFlashcardUseCase createFlashcardUseCase;

    public DeckController(
            GetDeckUseCase getDeckUseCase,
            ListDeckUseCase listDeckUseCase,
            CreateDeckUseCase createDeckUseCase,
            CreateFlashcardUseCase createFlashcardUseCase) {
        this.getDeckUseCase = getDeckUseCase;
        this.listDeckUseCase = listDeckUseCase;
        this.createDeckUseCase = createDeckUseCase;
        this.createFlashcardUseCase = createFlashcardUseCase;
    }

    @GetMapping
    @Operation(summary = "List decks", description = "Retrieve all decks with optional name filtering")
    @ApiResponse(responseCode = "200", description = "Decks retrieved successfully")
    public List<DeckDto.DeckResponse> getDecks(@Parameter(description = "Filter decks with optional name filter (case-insensitive") @RequestParam(required = false) String nameFilter) {
        return listDeckUseCase
                .execute(new ListDeckCommand(nameFilter))
                .stream()
                .map(deck -> new DeckDto.DeckResponse(deck.deckId().uuid().toString(), deck.name(), deck.description()))
                .toList();
    }

    @PostMapping
    @Operation(summary = "Create deck", description = "Create a new deck")
    @ApiResponse(responseCode = "201", description = "Deck created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public DeckDto.DeckResponse createDeck(@Valid @RequestBody DeckDto.DeckRequest deckRequest) {
        Deck deck = createDeckUseCase.execute(new CreateDeckCommand(deckRequest.name(), deckRequest.description(), List.of()));
        return new DeckDto.DeckResponse(deck.deckId().uuid().toString(), deck.name(), deck.description());
    }

    @GetMapping("/{deckId}")
    @Operation(summary = "Get deck", description = "Retrieve a deck by its ID")
    @ApiResponse(responseCode = "200", description = "Deck retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Deck not owned")
    @ApiResponse(responseCode = "404", description = "Deck not found")
    public DeckDto.DeckResponse getDeck(@Parameter(description = "UUID of the deck to retrieve") @PathVariable String deckId) {
        Deck deck = getDeckUseCase.execute(new GetDeckCommand(DeckId.from(deckId)));
        return new DeckDto.DeckResponse(deck.deckId().uuid().toString(), deck.name(), deck.description());
    }

    @PostMapping("/{deckId}/flashcards")
    @Operation(summary = "Create flashcard", description = "Create a flashcard in a deck")
    @ApiResponse(responseCode = "201", description = "Flashcard created successfully")
    @ApiResponse(responseCode = "403", description = "Deck not owned")
    @ApiResponse(responseCode = "404", description = "Deck not found")
    @ResponseStatus(HttpStatus.CREATED)
    public FlashcardDto.FlashcardResponse createFlashcard(
            @PathVariable String deckId, @Valid @RequestBody FlashcardDto.FlashcardRequest flashcardRequest) {
        Flashcard flashcard = createFlashcardUseCase.execute(new CreateFlashcardCommand(
                DeckId.from(deckId), new Question(flashcardRequest.question()), new Answer(flashcardRequest.answer())));
        return new FlashcardDto.FlashcardResponse(
                flashcard.flashcardId().uuid().toString(),
                flashcard.question().value(),
                flashcard.answer().value());
    }
}
