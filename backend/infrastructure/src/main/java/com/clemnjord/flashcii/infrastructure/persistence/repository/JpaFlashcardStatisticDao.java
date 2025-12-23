package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardStatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFlashcardStatisticDao extends JpaRepository<FlashcardStatisticEntity, FlashcardEntityId> {}
