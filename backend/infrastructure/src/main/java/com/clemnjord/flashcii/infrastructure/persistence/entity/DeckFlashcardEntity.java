package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "decks_flashcards")
@Getter
@Setter
@NoArgsConstructor
public class DeckFlashcardEntity {

    @EmbeddedId
    private DeckFlashcardEntityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", insertable = false, updatable = false)
    private DeckEntity deck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(
                name = "flashcard_id",
                referencedColumnName = "flashcard_id",
                insertable = false,
                updatable = false),
        @JoinColumn(name = "owner_id", referencedColumnName = "owner_id", insertable = false, updatable = false)
    })
    private FlashcardEntity flashcard;

    public DeckFlashcardEntity(DeckEntity deck, FlashcardEntity flashcard) {
        this.id = new DeckFlashcardEntityId(
                deck.getId(),
                flashcard.getId().getFlashcardId(),
                flashcard.getId().getOwnerId());
        this.deck = deck;
        this.flashcard = flashcard;
    }
}
