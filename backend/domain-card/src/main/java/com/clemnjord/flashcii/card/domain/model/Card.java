package com.clemnjord.flashcii.card.domain.model;

import com.clemnjord.flashcii.shared.kernel.CardId;
import lombok.Builder;

@Builder
public record Card(CardId cardId,
                   String question,
                   String answer) {
}
