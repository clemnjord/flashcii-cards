package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.IListDeckUseCase;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.user.User;

import java.util.List;

@ApplicationService
public class ListDeckUseCase implements IListDeckUseCase {
    IDeckRepository deckRepository;
    ICurrentUserUseCase currentUserUseCase;

    public ListDeckUseCase(IDeckRepository deckRepository, ICurrentUserUseCase currentUserUseCase) {
        this.deckRepository = deckRepository;
        this.currentUserUseCase = currentUserUseCase;
    }

    @Override
    @ApplicationTransactional
    public List<Deck> execute(ListDeckCommand command) {
        User currentUserId = currentUserUseCase.getCurrentUser();

        return deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(currentUserId.userId(), command.nameFilter());
    }
}
