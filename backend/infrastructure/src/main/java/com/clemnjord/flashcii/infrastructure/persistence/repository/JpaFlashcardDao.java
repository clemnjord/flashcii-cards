package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaFlashcardDao extends JpaRepository<FlashcardEntity, UUID> {
    // Update this method to work with the new structure
    @Query("SELECT CASE WHEN COUNT(df) > 0 THEN true ELSE false END FROM DeckFlashcardEntity df WHERE df.flashcard.question = :question AND df.deck.uuid = :deckUUID")
    boolean existsByQuestionAndDeck_Uuid(@Param("question") String question, @Param("deckUUID") UUID deckUUID);

    // Add new methods for the many-to-many relationship
    @Query("SELECT f FROM FlashcardEntity f WHERE f.id.ownerId = :ownerId AND f.question LIKE %:questionFilter%")
    List<FlashcardEntity> findAllByOwnerIdAndQuestionContainsIgnoreCase(@Param("ownerId") UUID ownerId, @Param("questionFilter") String questionFilter);

}
