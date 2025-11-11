package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "flashcard")
public class FlashcardEntity {

    @EmbeddedId
    private FlashcardEntityId id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL)
    private List<DeckFlashcardEntity> deckFlashcards = new ArrayList<>();

    // Getters and setters
    public FlashcardEntityId getID() {
        return id;
    }

    public void setID(FlashcardEntityId id) {
        this.id = id;
    }

    // Utility methods
    public List<DeckEntity> getDecks() {
        return deckFlashcards.stream().map(DeckFlashcardEntity::getDeck).toList();
    }
}
