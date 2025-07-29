package com.clemnjord.flashcii.application.port.input;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.domain.model.Card;
import com.clemnjord.flashcii.domain.model.CollectionId;

@ApplicationService
public interface ICreateCardUseCase {
  record CreateCardCommand(CollectionId collectionId, String question, String answer) {}

  Card execute(CreateCardCommand command);
}
