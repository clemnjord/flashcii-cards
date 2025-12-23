package com.clemnjord.flashcii.domain.model.deck;

import com.clemnjord.flashcii.domain.exception.deck.InvalidDeckException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record Deck(DeckId deckId, String name, String description, UserId ownerId, Set<FlashcardId> flashcardIds) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    public Deck {
        Objects.requireNonNull(deckId, "Deck ID is required");
        Objects.requireNonNull(name, "Deck name is required");
        Objects.requireNonNull(ownerId, "Owner ID is required");

        name = name.trim();
        validateName(name);

        description = description != null ? description : "";

        flashcardIds = Set.copyOf(flashcardIds);
    }

    public static Deck createNew(String name, String description, UserId ownerId) {
        return new Deck(DeckId.generate(), name, description, ownerId, Set.of());
    }

    private static void validateName(String name) {
        if (name.length() < MIN_LENGTH) {
            throw new InvalidDeckException("Deck name too short (min " + MIN_LENGTH + " characters)");
        }
        if (name.length() > MAX_LENGTH) {
            throw new InvalidDeckException("Deck name too long (max " + MAX_LENGTH + " characters)");
        }
    }

    public Deck addFlashcard(FlashcardId flashcardId) {
        if (flashcardIds.contains(flashcardId)) {
            throw new FlashcardAlreadyExistsException("Flashcard already exists in deck");
        }

        Set<FlashcardId> newFlashcards = new HashSet<>(flashcardIds);
        newFlashcards.add(flashcardId);

        return new Deck(deckId, name, description, ownerId, newFlashcards);
    }
}
