package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.ListDeckUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;

import java.util.List;

@ApplicationService
public class ListDeckUseCaseImpl implements ListDeckUseCase {
    private final IDeckRepository deckRepository;
    private final IUserContextService userContextService;

    public ListDeckUseCaseImpl(IDeckRepository deckRepository, IUserContextService userContextService) {
        this.deckRepository = deckRepository;
        this.userContextService = userContextService;
    }

    @Override
    @ApplicationTransactional(readOnly = true)
    public List<Deck> execute(ListDeckCommand command) {
        User currentUserId = userContextService.getCurrentUser();

        return deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(currentUserId.userId(), command.nameFilter());
    }
}
