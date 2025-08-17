package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.Deck;


public interface CreateDeckUseCase {
    Deck execute(CreateDeckCommand command);
}
