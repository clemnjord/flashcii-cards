package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.output.IAuthorizationService;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.user.UnauthorizedException;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationService
@ApplicationTransactional
@AllArgsConstructor
public class CreateFlashcardUseCaseImpl implements CreateFlashcardUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateFlashcardUseCaseImpl.class);

    private final IFlashcardRepository flashcardRepository;
    private final IUserContextService userContextService;
    private final IAuthorizationService authorizationService;

    @Override
    public Flashcard execute(CreateFlashcardCommand command) {
        User currentUser = userContextService.getCurrentUser();

        if (!authorizationService.canManageResourceFor(currentUser, command.ownerId())) {
            throw new UnauthorizedException(
                    "User " + currentUser.userId().uuid() + " is not authorized to create flashcards for user "
                            + command.ownerId().uuid());
        }

        logger.debug("Creating flashcard with question '{}' and answer '{}'", command.question(), command.answer());

        // Create and save the new flashcard
        Flashcard flashcard = Flashcard.createNew(command.question(), command.answer());
        flashcardRepository.save(flashcard, command.ownerId());

        // Return the saved flashcard
        return flashcard;
    }
}
