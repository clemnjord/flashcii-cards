package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class DeckFlashcardEntityId implements Serializable {

    private UUID deckId;

    @Embedded
    private FlashcardEntityId flashcardId;

    public DeckFlashcardEntityId() {}

    public DeckFlashcardEntityId(UUID deckId, FlashcardEntityId flashcardId) {
        this.deckId = deckId;
        this.flashcardId = flashcardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckFlashcardEntityId that = (DeckFlashcardEntityId) o;
        return Objects.equals(deckId, that.deckId) &&
                Objects.equals(flashcardId, that.flashcardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deckId, flashcardId);
    }
}