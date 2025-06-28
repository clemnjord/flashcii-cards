package com.clemnjord.flashcii.service.flashcard.usecase;

import com.clemnjord.flashcii.application.flashcard.command.CreateCollectionCommand;
import com.clemnjord.flashcii.application.flashcard.usecase.ICreateCollectionUseCase;
import com.clemnjord.flashcii.application.user.usecase.ICurrentUserUseCase;
import com.clemnjord.flashcii.domain.flashcard.exception.CollectionAlreadyExistsException;
import com.clemnjord.flashcii.domain.flashcard.model.Collection;
import com.clemnjord.flashcii.domain.flashcard.repository.ICollectionRepository;
import com.clemnjord.flashcii.domain.user.model.User;
import java.util.Collections;

public class CreateCollectionUseCase implements ICreateCollectionUseCase {

  private final ICollectionRepository collectionRepository;
  private final ICurrentUserUseCase currentUserUseCase;

  public CreateCollectionUseCase(
      ICollectionRepository collectionRepository, ICurrentUserUseCase currentUserUseCase) {
    this.collectionRepository = collectionRepository;
    this.currentUserUseCase = currentUserUseCase;
  }

  public Collection execute(CreateCollectionCommand command) {
    // TODO: Should I check if the User exists?
    User currentUser = currentUserUseCase.getCurrentUser();

    if (collectionRepository.existsByName(command.name())) {
      throw new CollectionAlreadyExistsException(command.name());
    }
    // Create a new collection
    Collection collection =
        new Collection(
            null,
            command.name(),
            command.description(),
            currentUser.userId(),
            Collections.emptyList());

    // Save the collection to the repository
    return collectionRepository.save(collection);
  }
}
