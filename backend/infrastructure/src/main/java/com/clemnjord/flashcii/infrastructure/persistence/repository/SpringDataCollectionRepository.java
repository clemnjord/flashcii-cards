package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataCollectionRepository extends JpaRepository<CollectionEntity, UUID> {
    boolean existsByName(String name);

    Optional<CollectionEntity> findByName(String name);
}
