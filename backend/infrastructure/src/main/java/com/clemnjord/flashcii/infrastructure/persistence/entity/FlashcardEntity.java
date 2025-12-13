package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "flashcards")
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardEntity {

    @EmbeddedId
    private FlashcardEntityId id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @OneToMany(mappedBy = "flashcard", cascade = CascadeType.ALL)
    private List<DeckFlashcardEntity> deckFlashcards = new ArrayList<>();

    // Utility methods
    public List<DeckEntity> getDecks() {
        return deckFlashcards.stream().map(DeckFlashcardEntity::getDeck).toList();
    }
}
