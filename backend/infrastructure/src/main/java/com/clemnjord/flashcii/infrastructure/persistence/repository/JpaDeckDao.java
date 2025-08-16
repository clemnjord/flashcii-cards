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

    Optional<DeckEntity> findByNameAndOwner_Uuid(String name, UUID ownerUuid);

    Optional<DeckEntity> findByUuidAndOwner_Uuid(UUID uuid, UUID ownerUuid);

    List<DeckEntity> findAllByOwner_UuidAndNameContainsIgnoreCase(UUID ownerId, String nameFilter);

    boolean existsByNameAndOwner_Uuid(String name, UUID ownerUuid);

    boolean existsByUuidAndOwner_Uuid(UUID id, UUID ownerUuid);
}
