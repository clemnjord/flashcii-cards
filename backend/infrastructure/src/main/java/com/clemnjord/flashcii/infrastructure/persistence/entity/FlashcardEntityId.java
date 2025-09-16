package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardEntityId that = (FlashcardEntityId) o;
        return Objects.equals(flashcardId, that.flashcardId) && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flashcardId, ownerId);
    }
}
