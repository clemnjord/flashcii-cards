package com.clemnjord.flashcii.domain.model;

import java.util.List;

public record Collection(
    CollectionId collectionId,
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
