package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaDeckDao extends JpaRepository<DeckEntity, UUID> {
    boolean existsByName(String name);

    Optional<DeckEntity> findByName(String name);

    Optional<DeckEntity> findByUuid(UUID uuid);

    List<DeckEntity> findAllByOwner_UuidAndNameContainsIgnoreCase(UUID ownerId, String nameFilter);

}
