package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaDeckDao extends JpaRepository<DeckEntity, UUID> {
    boolean existsByName(String name);

    @Query("""
        SELECT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE LOWER(d.name) = LOWER(:name)
        AND d.owner.uuid = :ownerUuid
        """)
    Optional<DeckEntity> findByNameAndOwner_Uuid(@Param("name") String name, @Param("ownerUuid") UUID ownerUuid);

    @Query("""
        SELECT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE d.uuid = :uuid
        AND d.owner.uuid = :ownerUuid
        """)
    Optional<DeckEntity> findByUuidAndOwner_Uuid(@Param("uuid") UUID uuid, @Param("ownerUuid") UUID ownerUuid);

    @Query("""
        SELECT DISTINCT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE d.owner.uuid = :ownerId
        AND LOWER(d.name) LIKE LOWER(CONCAT('%', :nameFilter, '%'))
        """)
    List<DeckEntity> findAllByOwner_UuidAndNameContainsIgnoreCase(
            @Param("ownerId") UUID ownerId, @Param("nameFilter") String nameFilter);

    boolean existsByNameAndOwner_Uuid(String name, UUID ownerUuid);

    boolean existsByUuidAndOwner_Uuid(UUID id, UUID ownerUuid);
}
