package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class FlashcardEntityId implements Serializable {
    private UUID flashcardId;
    private UUID ownerId;

    public FlashcardEntityId(UUID flashcardId, UUID ownerId) {
        this.flashcardId = flashcardId;
        this.ownerId = ownerId;
    }

    protected FlashcardEntityId() {}
}
