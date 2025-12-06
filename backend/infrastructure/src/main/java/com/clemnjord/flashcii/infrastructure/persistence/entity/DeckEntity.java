package com.clemnjord.flashcii.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "deck")
@Getter
@Setter
@NoArgsConstructor
public class DeckEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Getter(AccessLevel.NONE) // Don't generate getter for this field
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeckFlashcardEntity> deckFlashcards = new ArrayList<>();

    // Getters and setters
    public UUID getUUID() {
        return uuid;
    }

    public void setId(UUID uuid) {
        this.uuid = uuid;
    }

    public List<FlashcardEntity> getFlashcards() {
        return deckFlashcards.stream().map(DeckFlashcardEntity::getFlashcard).toList();
    }

    public void addFlashcard(FlashcardEntity flashcard) {
        deckFlashcards.add(new DeckFlashcardEntity(this, flashcard));
    }
}
