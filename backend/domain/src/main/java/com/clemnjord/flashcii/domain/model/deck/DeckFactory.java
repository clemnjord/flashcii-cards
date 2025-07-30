package com.clemnjord.flashcii.domain.model.deck;

import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.HashSet;
import java.util.Set;

public class DeckFactory {

    public static Deck createEmpty(String name, String description, UserId ownerId) {
        return new Deck(null, name, description, ownerId, new HashSet<>());
    }

    public static Deck createWithFlashcards(String name, String description, UserId ownerId, Set<FlashcardId> flashcardIds) {
        return new Deck(null, name, description, ownerId, new HashSet<>(flashcardIds));
    }
}