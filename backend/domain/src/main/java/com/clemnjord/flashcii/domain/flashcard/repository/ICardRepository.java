package com.clemnjord.flashcii.domain.flashcard.repository;

import com.clemnjord.flashcii.domain.flashcard.model.Card;

public interface ICardRepository {
  Card save(Card card);
}
