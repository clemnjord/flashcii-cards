package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Objects;

public record CreateFlashcardCommand(UserId ownerId, Question question, Answer answer) {

    public CreateFlashcardCommand {
        Objects.requireNonNull(ownerId, "Owner ID cannot be null");
        Objects.requireNonNull(question, "Question cannot be null");
        Objects.requireNonNull(answer, "Answer cannot be null");
    }
}
