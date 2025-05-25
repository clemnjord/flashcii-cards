package com.clemnjord.flashcii.service.flashcard.usecase;

import com.clemnjord.flashcii.domain.flashcard.CollectionAlreadyExistsException;
import com.clemnjord.flashcii.domain.flashcard.model.Collection;
import com.clemnjord.flashcii.domain.flashcard.repository.ICollectionRepository;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.service.flashcard.command.CreateCollectionCommand;
import com.clemnjord.flashcii.service.user.usecase.ICurrentUserUseCase;

import java.util.Collections;

public class CreateCollectionUseCase {

    private final ICollectionRepository collectionRepository;
    private final ICurrentUserUseCase currentUserUseCase;

    public CreateCollectionUseCase(ICollectionRepository collectionRepository, ICurrentUserUseCase currentUserUseCase) {
        this.collectionRepository = collectionRepository;
        this.currentUserUseCase = currentUserUseCase;
    }

    public Collection execute(CreateCollectionCommand command) {
        // TODO: Should I check if the User exists?
        User currentUser = currentUserUseCase.getCurrentUser();

        if (collectionRepository.findByName(command.name()).isPresent()) {
            throw new CollectionAlreadyExistsException(command.name());
        }
        // Create a new collection
        Collection collection = new Collection(null,
                command.name(),
                command.description(),
                currentUser.userId(),
                Collections.emptyList()
        );

        // Save the collection to the repository
        return collectionRepository.save(collection);
    }

}
