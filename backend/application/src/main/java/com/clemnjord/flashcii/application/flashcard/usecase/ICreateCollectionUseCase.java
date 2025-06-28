package com.clemnjord.flashcii.application.flashcard.usecase;

import com.clemnjord.flashcii.application.flashcard.command.CreateCollectionCommand;
import com.clemnjord.flashcii.domain.flashcard.model.Collection;

public interface ICreateCollectionUseCase {
  Collection execute(CreateCollectionCommand command);
}
