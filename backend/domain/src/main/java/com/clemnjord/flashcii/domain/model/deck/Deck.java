package com.clemnjord.flashcii.domain.model.deck;

import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Set;

public class Deck {
    private DeckId deckId;
    private String name;
    private String description;
    private UserId ownerId;
    private Set<FlashcardId> flashcardIds;

    public Deck(DeckId deckId, String name, String description, UserId ownerId, Set<FlashcardId> flashcardIds) {
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


// TODO: look into this - Add missing business logic and improve input cleanup

//public class Deck {
//    private static final int MAX_FLASHCARDS = 200;
//    private static final int MAX_NAME_LENGTH = 100;
//
//    // ... existing fields ...
//
//    public Deck(DeckId deckId, String name, String description, UserId ownerId, Set<FlashcardId> flashcardIds) {
//        validateName(name);
//        validateDescription(description);
//        Objects.requireNonNull(ownerId, "Owner ID is required");
//
//        this.deckId = deckId;
//        this.name = name.trim();
//        this.description = description.trim();
//        this.ownerId = ownerId;
//        this.flashcardIds = new HashSet<>(flashcardIds);
//    }
//
//    // Business methods
//    public boolean isEmpty() {
//        return flashcardIds.isEmpty();
//    }
//
//    public boolean isFull() {
//        return flashcardIds.size() >= MAX_FLASHCARDS;
//    }
//
//    public int getFlashcardCount() {
//        return flashcardIds.size();
//    }
//
//    public boolean canAddFlashcard() {
//        return !isFull();
//    }
//
//    public void addFlashcard(FlashcardId flashcardId) {
//        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");
//
//        if (isFull()) {
//            throw new DeckFullException("Cannot add more flashcards. Maximum is " + MAX_FLASHCARDS);
//        }
//
//        if (flashcardIds.contains(flashcardId)) {
//            throw new FlashcardAlreadyExistsException("Flashcard already exists in deck");
//        }
//
//        flashcardIds.add(flashcardId);
//    }
//
//    public void removeFlashcard(FlashcardId flashcardId) {
//        if (!flashcardIds.remove(flashcardId)) {
//            throw new FlashcardNotFoundException("Flashcard not found in deck");
//        }
//    }
//
//    private void validateName(String name) {
//        Objects.requireNonNull(name, "Deck name is required");
//        if (name.isBlank()) {
//            throw new IllegalArgumentException("Deck name cannot be blank");
//        }
//        if (name.length() > MAX_NAME_LENGTH) {
//            throw new IllegalArgumentException("Deck name too long (maximum " + MAX_NAME_LENGTH + " characters)");
//        }
//    }
//
//    private void validateDescription(String description) {
//        Objects.requireNonNull(description, "Deck description is required");
//        // Description can be empty but not null
//    }
//}