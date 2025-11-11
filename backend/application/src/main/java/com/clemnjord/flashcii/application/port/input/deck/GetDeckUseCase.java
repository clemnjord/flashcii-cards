package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.Deck;

public interface GetDeckUseCase {
    Deck execute(GetDeckCommand command);
}
