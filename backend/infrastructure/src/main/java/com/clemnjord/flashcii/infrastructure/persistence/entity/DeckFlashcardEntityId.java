package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DeckFlashcardEntityId implements Serializable {

    @Column(name = "deck_id")
    private UUID deckId;

    @Column(name = "flashcard_id")
    private UUID flashcardId;

    @Column(name = "owner_id")
    private UUID ownerId;

    public DeckFlashcardEntityId() {}

    public DeckFlashcardEntityId(UUID deckId, UUID flashcardId, UUID ownerId) {
        this.deckId = deckId;
        this.flashcardId = flashcardId;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckFlashcardEntityId that = (DeckFlashcardEntityId) o;
        return Objects.equals(deckId, that.deckId)
                && Objects.equals(flashcardId, that.flashcardId)
                && Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, flashcardId, ownerId);
    }
}
