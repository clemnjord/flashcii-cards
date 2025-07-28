package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.Card;
import com.clemnjord.flashcii.domain.model.CardId;
import com.clemnjord.flashcii.domain.model.CollectionId;
import java.util.Optional;

public interface ICardRepository {
  Card save(Card card);

  Optional<Card> findById(CardId id);

  boolean existsByQuestionAndCollectionId(String question, CollectionId collectionId);
}
