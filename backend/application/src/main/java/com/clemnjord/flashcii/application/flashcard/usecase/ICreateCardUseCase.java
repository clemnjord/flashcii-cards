package com.clemnjord.flashcii.application.flashcard.usecase;

import com.clemnjord.flashcii.application.flashcard.command.CreateCardCommand;
import com.clemnjord.flashcii.domain.flashcard.model.Card;

public interface ICreateCardUseCase {
  Card execute(CreateCardCommand command);
}
