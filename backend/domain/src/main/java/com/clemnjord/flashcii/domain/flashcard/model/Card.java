package com.clemnjord.flashcii.domain.flashcard.model;

import lombok.Builder;

@Builder
public record Card(CardId cardId, String question, String answer) {}
