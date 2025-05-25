package com.clemnjord.flashcii.domain.flashcard.model;


import com.clemnjord.flashcii.domain.user.model.UserId;

import java.util.List;

public record Collection(CollectionId collectionId,
                         String name,
                         String description,
                         UserId ownerId,
                         List<CardId> cards) {

    public void addCard(CardId cardId) {
        if (!cards.contains(cardId)) {
            cards.add(cardId);
        }
    }
}
