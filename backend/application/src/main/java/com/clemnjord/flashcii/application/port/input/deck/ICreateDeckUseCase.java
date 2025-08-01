package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.deck.Deck;

import java.util.List;
import java.util.Objects;

@ApplicationService
public interface ICreateDeckUseCase {
    record CreateDeckCommand(String name, String description, List<String> tags) {

        public CreateDeckCommand(String name, String description) {
            this(name, description, List.of());
        }

        public CreateDeckCommand {
            Objects.requireNonNull(name, "Deck name cannot be null");
            Objects.requireNonNull(description, "Deck description cannot be null");
            Objects.requireNonNull(tags, "Deck tags cannot be null");
        }
    }

    Deck execute(CreateDeckCommand command);
}
