package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;

public interface CreateFlashcardUseCase {
    Flashcard execute(CreateFlashcardCommand command);
}
