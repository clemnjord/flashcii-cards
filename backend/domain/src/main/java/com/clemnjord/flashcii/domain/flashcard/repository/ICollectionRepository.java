package com.clemnjord.flashcii.domain.flashcard.repository;

import com.clemnjord.flashcii.domain.flashcard.model.Collection;
import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;
import java.util.Optional;

public interface ICollectionRepository {

  Collection save(Collection collection);

  boolean existsByName(String name);

  Optional<Collection> findByName(String name);

  Optional<Collection> findById(CollectionId id);
}
