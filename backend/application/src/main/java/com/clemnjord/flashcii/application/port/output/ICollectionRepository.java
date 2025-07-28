package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.Collection;
import com.clemnjord.flashcii.domain.model.CollectionId;
import java.util.Optional;

public interface ICollectionRepository {

  Collection save(Collection collection);

  boolean existsByName(String name);

  boolean existsById(CollectionId id);

  Optional<Collection> findByName(String name);

  Optional<Collection> findById(CollectionId id);
}
