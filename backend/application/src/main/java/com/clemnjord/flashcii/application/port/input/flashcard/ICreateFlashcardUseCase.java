package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;

@ApplicationService
public interface ICreateFlashcardUseCase {
    Flashcard execute(CreateFlashcardCommand command);

}
