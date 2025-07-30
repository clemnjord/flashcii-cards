package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "deck")
public class DeckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlashcardEntity> flashcards = new ArrayList<>();

    // Getters and setters

    public UUID getUUID() {
        return uuid;
    }

    public void setId(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FlashcardEntity> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<FlashcardEntity> cards) {
        this.flashcards = cards;
    }

    // Utility methods for managing the bidirectional relationship
    public void addFlashcard(FlashcardEntity flashcard) {
        flashcards.add(flashcard);
        flashcard.setDeck(this);
    }

    public void removeCard(FlashcardEntity flashcard) {
        flashcards.remove(flashcard);
        flashcard.setDeck(null);
    }
}
