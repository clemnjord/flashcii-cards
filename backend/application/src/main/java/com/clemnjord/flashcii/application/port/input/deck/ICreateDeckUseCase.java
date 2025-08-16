package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.deck.Deck;


@ApplicationService
public interface ICreateDeckUseCase {
    Deck execute(CreateDeckCommand command);
}
