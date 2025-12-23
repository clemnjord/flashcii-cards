package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import io.github.openspacedrepetition.Card;
import java.util.Optional;

public interface IFlashcardStatisticRepository {
    void save(FlashcardId flashcardId, UserId userId, Card statistic);

    Optional<Card> get(FlashcardId flashcardId, UserId userId);
}
