package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataCardRepository extends JpaRepository<FlashcardEntity, UUID> {
    boolean existsByQuestionAndDeck_Uuid(String question, UUID deckUUID);
}
