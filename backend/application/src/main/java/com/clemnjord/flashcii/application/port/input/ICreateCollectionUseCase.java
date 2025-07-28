package com.clemnjord.flashcii.application.port.input;

import com.clemnjord.flashcii.domain.model.Collection;

import java.util.List;

public interface ICreateCollectionUseCase {
    record CreateCollectionCommand(String name, String description, List<String> tags) {}
    Collection execute(CreateCollectionCommand command);
}
