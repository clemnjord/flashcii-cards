package com.clemnjord.flashcii.application.port.input.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.deck.Deck;

import java.util.List;
import java.util.Objects;

@ApplicationService
public interface ICreateDeckUseCase {
    Deck execute(CreateDeckCommand command);

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
}


// TODO: Look into this - add input validation
//record CreateDeckCommand(String name, String description, List<String> tags) {
//
//  public CreateDeckCommand(String name, String description) {
//    this(name, description, List.of());
//  }
//
//  public CreateDeckCommand {
//    validateName(name);
//    validateDescription(description);
//    Objects.requireNonNull(tags, "Tags cannot be null");
//
//    // Clean up the data
//    name = name.trim();
//    description = description.trim();
//    tags = tags.stream()
//            .filter(tag -> !tag.isBlank())
//            .map(String::trim)
//            .distinct()
//            .collect(toList());
//  }
//
//  private static void validateName(String name) {
//    Objects.requireNonNull(name, "Deck name cannot be null");
//    if (name.isBlank()) {
//      throw new IllegalArgumentException("Deck name cannot be blank");
//    }
//  }
//
//  private static void validateDescription(String description) {
//    Objects.requireNonNull(description, "Deck description cannot be null");
//  }
//}
