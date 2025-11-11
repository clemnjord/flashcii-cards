package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import java.util.List;

public interface ListDeckUseCase {

    List<Deck> execute(ListDeckCommand command);
}
