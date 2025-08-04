package com.clemnjord.flashcii.infrastructure.persistence.mapper;

import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.infrastructure.persistence.entity.DeckEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import org.springframework.stereotype.Component;

@Component
public class FlashcardMapper {
    public Flashcard toDomain(FlashcardEntity entity) {
        return Flashcard.restore(
                new FlashcardId(entity.getUUID()),
                new Question(entity.getQuestion()),
                new Answer(entity.getAnswer())
        );
    }

    public FlashcardEntity toEntity(Flashcard flashcard, DeckEntity deckEntity) {
        FlashcardEntity entity = new FlashcardEntity();

        entity.setUUID(flashcard.flashcardId().uuid());
        entity.setQuestion(flashcard.question().value());
        entity.setAnswer(flashcard.answer().value());
        entity.setDeck(deckEntity);
        return entity;
    }
}