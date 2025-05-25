package com.clemnjord.flashcii.domain.quiz.model;

import com.clemnjord.flashcii.domain.flashcard.model.Card;
import com.clemnjord.flashcii.domain.flashcard.model.CollectionId;

import java.util.List;

public record Quiz(QuizId id,
                   CollectionId collectionId,
                   QuizConfiguration configuration,
                   List<Card> cards) {
}
