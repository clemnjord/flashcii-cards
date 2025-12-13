package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckFlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckFlashcardEntityId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDeckFlashcardDao extends JpaRepository<DeckFlashcardEntity, DeckFlashcardEntityId> {

    // Find all decks containing a specific flashcard for an owner
    @Query("""
        SELECT df.deck FROM DeckFlashcardEntity df
        WHERE df.flashcard.id.flashcardId = :flashcardId AND df.flashcard.id.ownerId = :ownerId
        """)
    List<DeckEntity> findDecksByFlashcardIdAndOwnerId(
            @Param("flashcardId") UUID flashcardId, @Param("ownerId") UUID ownerId);
}
