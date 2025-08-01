package com.clemnjord.flashcii.domain.model.deck;

import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Objects;
import java.util.Set;

public class Deck {
    private DeckId deckId;
    private String name;
    private String description;
    private UserId ownerId;
    private Set<FlashcardId> flashcardIds;

    public Deck(DeckId deckId, String name, String description, UserId ownerId, Set<FlashcardId> flashcardIds) {
        Objects.requireNonNull(ownerId, "Owner ID is required");

        this.deckId = deckId;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.flashcardIds = flashcardIds;
    }

    public Set<FlashcardId> getFlashcardIds() {
        return Set.copyOf(flashcardIds); // Return immutable copy
    }

    public void setFlashcardIds(Set<FlashcardId> flashcardIds) {
        this.flashcardIds = flashcardIds;
    }

    public DeckId getDeckId() {
        return deckId;
    }

    public void setDeckId(DeckId deckId) {
        this.deckId = deckId;
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

    public UserId getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserId ownerId) {
        this.ownerId = ownerId;
    }

    public void addFlashcard(FlashcardId flashcardId) {
        if (!flashcardIds.contains(flashcardId)) {
            flashcardIds.add(flashcardId);
        } else {
            throw new FlashcardAlreadyExistsException("Flashcard already exists in deck");
        }
    }
}