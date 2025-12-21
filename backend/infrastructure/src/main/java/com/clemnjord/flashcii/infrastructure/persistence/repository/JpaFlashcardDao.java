package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFlashcardDao extends JpaRepository<FlashcardEntity, FlashcardEntityId> {
    @Query("""
            SELECT f FROM FlashcardEntity f
            JOIN f.deckFlashcards df
            JOIN df.deck d
            LEFT JOIN FlashcardStatisticEntity fs ON fs.id = f.id
            WHERE d.id IN :deckIds
            AND d.owner.id = :ownerId
            AND (fs.due IS NULL OR fs.due <= :timeGate)
            ORDER BY fs.due NULLS FIRST
    """)

    //    @Query("""
    //    SELECT f FROM FlashcardEntity f
    //    JOIN f.deckFlashcards df
    //    JOIN df.deck d
    //    JOIN FlashcardStatisticEntity fs ON fs.id = f.id
    //    WHERE d.id IN :deckIds
    //    AND d.owner.id = :ownerId
    //    AND fs.due <= :timeGate
    //    ORDER BY fs.due ASC
    //    """)
    List<FlashcardEntity> findDueFlashcardsByDeckIdsAndOwnerId(
            @Param("deckIds") Set<UUID> deckIds, @Param("ownerId") UUID ownerId, @Param("timeGate") Instant timeGate);

    List<FlashcardEntity> findAllByIdIn(List<FlashcardEntityId> ids);
}
