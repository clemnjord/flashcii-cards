package com.clemnjord.flashcii.card.domain.model;

import com.clemnjord.flashcii.shared.kernel.CardId;
import com.clemnjord.flashcii.shared.kernel.CollectionId;
import com.clemnjord.flashcii.shared.kernel.UserId;

import java.util.List;

public record Collection(CollectionId collectionId,
                         String name,
                         String description,
                         UserId ownerId,
                         List<CardId> cards) {
}
