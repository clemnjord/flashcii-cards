package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.ICreateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationService
@ApplicationTransactional
public class CreateFlashcardUseCase implements ICreateFlashcardUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateFlashcardUseCase.class);

    private final IFlashcardRepository flashcardRepository;
    private final IDeckRepository deckRepository;
    private final IUserContextService userContextService;

    public CreateFlashcardUseCase(IFlashcardRepository flashcardRepository, IDeckRepository deckRepository, IUserContextService userContextService) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.userContextService = userContextService;
    }

    @Override
    public Flashcard execute(CreateFlashcardCommand command) {
        User currentUser = userContextService.getCurrentUser();

        logger.debug("Creating flashcard with question '{}' and answer '{}'",
                command.question(),
                command.answer()
        );

        // Check if the deck exists
        var deck = deckRepository.findByIdAndOwnerId(command.deckId(), currentUser.userId())
                                 .orElseThrow(() -> new DeckNotFoundException("Deck not found with ID: " + command
                                         .deckId()
                                         .uuid()));

        // Check if flashcard already exists in the deck
        if (flashcardRepository.existsByQuestionAndDeckId(command.question(), command.deckId())) {
            throw new FlashcardAlreadyExistsException("A flashcard with this question already exists in the deck");
        }

        // Create and save the new flashcard
        Flashcard flashcard = Flashcard.createNew(command.question(), command.answer());
        flashcardRepository.save(flashcard, command.deckId());

        // Associate the saved flashcard's ID to the deck
        deck = deck.addFlashcard(flashcard.flashcardId());

        // Save the updated deck
        deckRepository.save(deck);

        // Return the saved flashcard
        return flashcard;
    }
}
