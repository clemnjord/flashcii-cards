package com.clemnjord.flashcii.domain.model;

import lombok.Builder;

import java.util.Objects;

public record Card(CardId cardId, Question question, Answer answer) {

    @Builder
    public Card {
        Objects.requireNonNull(question, "Question cannot be null");
        Objects.requireNonNull(answer, "Answer cannot be null");
    }
}
