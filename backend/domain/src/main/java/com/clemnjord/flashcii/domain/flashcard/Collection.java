package com.clemnjord.flashcii.domain.flashcard;


import com.clemnjord.flashcii.domain.user.model.UserId;

import java.util.List;

public record Collection(CollectionId collectionId,
                         String name,
                         String description,
                         UserId ownerId,
                         List<CardId> cards) {
}
