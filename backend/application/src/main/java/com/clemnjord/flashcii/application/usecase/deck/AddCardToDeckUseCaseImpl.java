package com.clemnjord.flashcii.application.usecase.deck;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckCommand;
import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.user.User;
import lombok.AllArgsConstructor;

@ApplicationService
@AllArgsConstructor
public class AddCardToDeckUseCaseImpl implements AddCardToDeckUseCase {
    private final IDeckRepository deckRepository;
    private final IFlashcardRepository flashcardRepository;
    private final IUserContextService userContextService;

    @ApplicationTransactional
    public void execute(AddCardToDeckCommand command) {
        User currentUser = userContextService.getCurrentUser();

        Flashcard flashcard = flashcardRepository
                .findByFlashcardIdAndOwnerId(command.flashcardId(), currentUser.userId())
                .orElseThrow(() -> new FlashcardNotFoundException(
                        "Flashcard not found with ID: " + command.flashcardId().uuid()));

        Deck deck = deckRepository
                .findByIdAndOwnerId(command.deckId(), currentUser.userId())
                .orElseThrow(() -> new DeckNotFoundException(
                        "Deck not found with ID: " + command.deckId().uuid()));

        if (!deck.flashcardIds().contains(flashcard.flashcardId())) {
            deckRepository.addFlashcardToDeck(deck.deckId(), flashcard.flashcardId(), currentUser.userId());
        }
    }
}
