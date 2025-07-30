package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.ICreateDeckUseCase;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;

import java.util.Collections;

@ApplicationService
@ApplicationTransactional
public class CreateDeckUseCase implements ICreateDeckUseCase {

  private final IDeckRepository deckRepository;
  private final ICurrentUserUseCase currentUserUseCase;

  public CreateDeckUseCase(
          IDeckRepository deckRepository, ICurrentUserUseCase currentUserUseCase) {
    this.deckRepository = deckRepository;
    this.currentUserUseCase = currentUserUseCase;
  }

  @Override
  public Deck execute(CreateDeckCommand command) {
    // TODO: Should I check if the User exists?
    User currentUser = currentUserUseCase.getCurrentUser();

    if (deckRepository.existsByName(command.name())) {
      throw new DeckAlreadyExistsException(
              "Deck with name '" + command.name() + "' already exists.");
    }
    // Create a new deck
    Deck deck =
            new Deck(
            null,
            command.name(),
            command.description(),
            currentUser.userId(),
                    Collections.emptySet());

    // Save the deck to the repository
    return deckRepository.save(deck);
  }
}
