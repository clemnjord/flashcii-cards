package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationService
@AllArgsConstructor
public class CreateDeckUseCaseImpl implements CreateDeckUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateDeckUseCaseImpl.class);

    private final IDeckRepository deckRepository;
    private final IUserContextService userContextService;

    @Override
    @ApplicationTransactional
    public Deck execute(CreateDeckCommand command) {
        Objects.requireNonNull(command, "CreateDeckCommand cannot be null");

        User currentUser = userContextService.getCurrentUser();
        logger.debug(
                "User '{}' creating deck with name '{}'", currentUser.username().value(), command.name());

        validateDeckCreationRules(command, currentUser.userId());

        Deck deck = Deck.createNew(command.name(), command.description(), currentUser.userId());
        deckRepository.save(deck);

        logger.debug("Successfully created deck with ID: {}", deck.deckId().uuid());
        return deck;
    }

    private void validateDeckCreationRules(CreateDeckCommand command, UserId ownerId) {
        if (deckRepository.existsByNameAndOwnerId(command.name(), ownerId)) {
            throw new DeckAlreadyExistsException("Deck with name '" + command.name() + "' already exists.");
        }
    }
}
