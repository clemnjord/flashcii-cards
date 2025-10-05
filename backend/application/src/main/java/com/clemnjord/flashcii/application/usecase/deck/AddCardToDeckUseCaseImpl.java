package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.output.IAuthorizationService;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UnauthorizedException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.user.User;

@ApplicationService
public class AddCardToDeckUseCaseImpl implements AddCardToDeckUseCase {
    private final IDeckRepository deckRepository;
    private final IFlashcardRepository flashcardRepository;
    private final IUserContextService userContextService;
    private final IAuthorizationService authorizationService;

    public AddCardToDeckUseCaseImpl(
            IDeckRepository deckRepository,
            IFlashcardRepository flashcardRepository,
            IUserContextService userContextService,
            IAuthorizationService authorizationService) {
        this.deckRepository = deckRepository;
        this.flashcardRepository = flashcardRepository;
        this.userContextService = userContextService;
        this.authorizationService = authorizationService;
    }

    @Override
    @ApplicationTransactional
    public void execute(AddCardToDeckCommand command) {
        User currentUser = userContextService.getCurrentUser();

        if (!authorizationService.canManageResourceFor(currentUser, command.ownerId())) {
            throw new UnauthorizedException(
                    "User " + currentUser.userId().uuid() +
                            " is not authorized to add flashcards for user " + command.ownerId().uuid());
        }

        Flashcard flashcard = flashcardRepository
                .findByFlashcardIdAndOwnerId(command.flashcardId(), command.ownerId())
                .orElseThrow(() -> new FlashcardNotFoundException(
                        "Flashcard not found with ID: " + command.flashcardId().uuid()));

        Deck deck = deckRepository
                .findByIdAndOwnerId(command.deckId(), command.ownerId())
                .orElseThrow(() -> new DeckNotFoundException(
                        "Deck not found with ID: " + command.deckId().uuid()));

        if (!deck.flashcardIds().contains(flashcard.flashcardId())) {
            deckRepository.addFlashcardToDeck(deck.deckId(), flashcard.flashcardId(), command.ownerId());
        }
    }
}
