package com.clemnjord.flashcii.domain.model.flashcard;

import java.util.Objects;

public record Flashcard(FlashcardId flashcardId, Question question, Answer answer) {

    public Flashcard {
        Objects.requireNonNull(flashcardId, "Flashcard ID cannot be null");
        Objects.requireNonNull(question, "Question cannot be null");
        Objects.requireNonNull(answer, "Answer cannot be null");
    }

    public static Flashcard createNew(Question question, Answer answer) {
        return new Flashcard(FlashcardId.generate(), question, answer);
    }

    public static Flashcard restore(FlashcardId flashcardId, Question question, Answer answer) {
        return new Flashcard(flashcardId, question, answer);
    }
}
