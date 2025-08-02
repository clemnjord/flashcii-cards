package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.ICreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.IListDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.web.dto.DeckDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/decks")
public class DeckController {

    IListDeckUseCase listDeckUseCase;
    ICreateDeckUseCase createDeckUseCase;

    public DeckController(IListDeckUseCase listDeckUseCase, ICreateDeckUseCase createDeckUseCase) {
        this.listDeckUseCase = listDeckUseCase;
        this.createDeckUseCase = createDeckUseCase;
    }

    @GetMapping
    public List<DeckDto.DeckResponse> getDecks(@RequestParam(required = false) String nameFilter) {
        return listDeckUseCase
                .execute(new IListDeckUseCase.ListDeckCommand(nameFilter))
                .stream()
                .map(deck -> new DeckDto.DeckResponse(deck.getDeckId().uuid().toString(), deck.getName(), deck.getDescription()))
                .toList();
    }

    @PostMapping
    public DeckDto.DeckResponse createDeck(@RequestBody DeckDto.DeckRequest deckRequest) {
        Deck deck = createDeckUseCase.execute(new ICreateDeckUseCase.CreateDeckCommand(deckRequest.name(), deckRequest.description(), List.of()));
        return new DeckDto.DeckResponse(deck.getDeckId().uuid().toString(), deck.getName(), deck.getDescription());
    }
}
