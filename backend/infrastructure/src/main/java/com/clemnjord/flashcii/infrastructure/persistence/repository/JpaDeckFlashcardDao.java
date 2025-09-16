package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckFlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckFlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaDeckFlashcardDao extends JpaRepository<DeckFlashcardEntity, DeckFlashcardEntityId> {

    // Find all decks containing a specific flashcard for an owner
    @Query("SELECT df.deck FROM DeckFlashcardEntity df WHERE df.flashcard.id.flashcardId = :flashcardId AND df.flashcard.id.ownerId = :ownerId")
    List<DeckEntity> findDecksByFlashcardIdAndOwnerId(@Param("flashcardId") UUID flashcardId, @Param("ownerId") UUID ownerId);

    // Find all flashcards in a specific deck
    @Query("SELECT df.flashcard FROM DeckFlashcardEntity df WHERE df.deck.uuid = :deckId")
    List<FlashcardEntity> findFlashcardsByDeckId(@Param("deckId") UUID deckId);

    // Check if a flashcard exists in a specific deck
    boolean existsByDeck_UuidAndFlashcard_Id_FlashcardIdAndFlashcard_Id_OwnerId(UUID deckId, UUID flashcardId, UUID ownerId);

    // Find by deck and owner (useful for validation)
    @Query("SELECT df FROM DeckFlashcardEntity df WHERE df.deck.uuid = :deckId AND df.deck.owner.uuid = :ownerId")
    List<DeckFlashcardEntity> findByDeckIdAndOwnerId(@Param("deckId") UUID deckId, @Param("ownerId") UUID ownerId);
}
