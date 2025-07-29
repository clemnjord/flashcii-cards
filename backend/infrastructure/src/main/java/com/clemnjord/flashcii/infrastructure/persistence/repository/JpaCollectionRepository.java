package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.application.port.output.ICollectionRepository;
import com.clemnjord.flashcii.domain.model.Collection;
import com.clemnjord.flashcii.domain.model.CollectionId;
import com.clemnjord.flashcii.domain.model.UserId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.CollectionEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaCollectionRepository implements ICollectionRepository {
    private final SpringDataCollectionRepository springRepository;

    public JpaCollectionRepository(SpringDataCollectionRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Collection save(Collection collection) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setName(collection.name());
        collectionEntity.setDescription(collection.description());

        CollectionEntity saved = springRepository.save(collectionEntity);

        return new Collection(
                new CollectionId(saved.getUUID()),
                saved.getName(),
                saved.getDescription(),
                new UserId(UUID.randomUUID()),
                new ArrayList<>());
    }

    @Override
    public boolean existsByName(String name) {

        return springRepository.existsByName(name);
    }

    @Override
    public boolean existsById(CollectionId id) {
        return springRepository.existsById(id.uuid());
    }

    @Override
    public Optional<Collection> findByName(String name) {
        return springRepository
                .findByName(name)
                .map(
                        x ->
                                new Collection(
                                        new CollectionId(x.getUUID()),
                                        x.getName(),
                                        x.getDescription(),
                                        new UserId(UUID.randomUUID()),
                                        new ArrayList<>()));
    }

    @Override
    public Optional<Collection> findById(CollectionId id) {
        return springRepository
                .findById(id.uuid())
                .map(
                        x ->
                                new Collection(
                                        new CollectionId(x.getUUID()),
                                        x.getName(),
                                        x.getDescription(),
                                        new UserId(UUID.randomUUID()),
                                        new ArrayList<>()));
    }
}
