package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.GetDeckUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;
import lombok.AllArgsConstructor;

@ApplicationService
@AllArgsConstructor
public class GetDeckUseCaseImpl implements GetDeckUseCase {

    private final IDeckRepository deckRepository;
    private final IUserContextService userContextService;

    @Override
    @ApplicationTransactional(readOnly = true)
    public Deck execute(GetDeckCommand command) {
        User currentUser = userContextService.getCurrentUser();

        return deckRepository
                .findByIdAndOwnerId(command.deckId(), currentUser.userId())
                .orElseThrow(() -> new DeckNotFoundException(
                        "Deck not found with ID: " + command.deckId().uuid() + "."));
    }
}
