package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataCardRepository extends JpaRepository<CardEntity, UUID> {
    boolean existsByQuestionAndCollection_Uuid(String question, UUID collectionUUID);
}
