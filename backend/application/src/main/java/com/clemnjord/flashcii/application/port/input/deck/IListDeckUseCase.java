package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.domain.model.deck.Deck;

import java.util.List;

public interface IListDeckUseCase {

    List<Deck> execute(ListDeckCommand command);

    record ListDeckCommand(String nameFilter) {
        public ListDeckCommand {
            if (nameFilter == null) {
                nameFilter = "";
            }
        }
    }
}
