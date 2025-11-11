package com.clemnjord.flashcii.infrastructure.persistence.mapper;

import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntity;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class FlashcardMapper {
    public Flashcard toDomain(FlashcardEntity entity) {
        return Flashcard.restore(
                new FlashcardId(entity.getID().getFlashcardId()),
                new Question(entity.getQuestion()),
                new Answer(entity.getAnswer()));
    }

    public FlashcardEntity toEntity(Flashcard flashcard, UserEntity userEntity) {
        FlashcardEntity entity = new FlashcardEntity();

        entity.setID(new FlashcardEntityId(flashcard.flashcardId().uuid(), userEntity.getUuid()));
        entity.setQuestion(flashcard.question().value());
        entity.setAnswer(flashcard.answer().value());
        return entity;
    }
}
