package com.clemnjord.flashcii.application.usecase.collection;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.port.input.ICreateCollectionUseCase;
import com.clemnjord.flashcii.application.port.output.ICollectionRepository;
import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.domain.exception.collection.CollectionAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.Collection;
import com.clemnjord.flashcii.domain.model.User;

import java.util.Collections;

@ApplicationService
public class CreateCollectionUseCase implements ICreateCollectionUseCase {

  private final ICollectionRepository collectionRepository;
  private final ICurrentUserUseCase currentUserUseCase;

  public CreateCollectionUseCase(
      ICollectionRepository collectionRepository, ICurrentUserUseCase currentUserUseCase) {
    this.collectionRepository = collectionRepository;
    this.currentUserUseCase = currentUserUseCase;
  }

  @Override
  public Collection execute(CreateCollectionCommand command) {
    // TODO: Should I check if the User exists?
    User currentUser = currentUserUseCase.getCurrentUser();

    if (collectionRepository.existsByName(command.name())) {
      throw new CollectionAlreadyExistsException(
          "Collection with name '" + command.name() + "' already exists.");
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
