package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeckDao extends JpaRepository<DeckEntity, UUID> {
    @Query("""
        SELECT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE LOWER(d.name) = LOWER(:name)
        AND d.owner.id = :ownerId
        """)
    Optional<DeckEntity> findByNameAndOwner_Id(@Param("name") String name, @Param("ownerId") UUID ownerId);

    @Query("""
        SELECT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE d.id = :id
        AND d.owner.id = :ownerId
        """)
    Optional<DeckEntity> findByIdAndOwner_Id(@Param("id") UUID id, @Param("ownerId") UUID ownerId);

    @Query("""
        SELECT DISTINCT d FROM DeckEntity d
        LEFT JOIN FETCH d.deckFlashcards
        WHERE d.owner.id = :ownerId
        AND LOWER(d.name) LIKE LOWER(CONCAT('%', :nameFilter, '%'))
        """)
    List<DeckEntity> findAllByOwner_IdAndNameContainsIgnoreCase(
            @Param("ownerId") UUID ownerId, @Param("nameFilter") String nameFilter);

    boolean existsByNameAndOwner_Id(String name, UUID ownerId);

    boolean existsByIdAndOwner_Id(UUID id, UUID ownerId);
}
