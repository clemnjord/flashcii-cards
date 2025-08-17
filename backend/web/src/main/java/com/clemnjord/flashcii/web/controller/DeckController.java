package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.CreateDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.ICreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.IListDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckCommand;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.web.dto.DeckDto;
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

    IListDeckUseCase listDeckUseCase;
    ICreateDeckUseCase createDeckUseCase;

    public DeckController(IListDeckUseCase listDeckUseCase, ICreateDeckUseCase createDeckUseCase) {
        this.listDeckUseCase = listDeckUseCase;
        this.createDeckUseCase = createDeckUseCase;
    }

    @GetMapping
    @Operation(summary = "List decks", description = "Retrieve all decks with optional name filtering")
    @ApiResponse(responseCode = "200", description = "Decks retrieved successfully")
    public List<DeckDto.DeckResponse> getDecks(
            @Parameter(description = "Filter decks with optional name filter (case-insensitive")
            @RequestParam(required = false) String nameFilter) {
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
}
