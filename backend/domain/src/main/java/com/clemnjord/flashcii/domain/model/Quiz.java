package com.clemnjord.flashcii.domain.model;

import java.util.List;

public record Quiz(
    QuizId id, CollectionId collectionId, QuizConfiguration configuration, List<Card> cards) {}
