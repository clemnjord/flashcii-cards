package com.clemnjord.flashcii.domain.flashcard;

import lombok.Builder;

@Builder
public record Card(CardId cardId,
                   String question,
                   String answer) {
}
