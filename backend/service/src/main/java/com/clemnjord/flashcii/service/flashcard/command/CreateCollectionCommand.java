package com.clemnjord.flashcii.service.flashcard.command;

import java.util.List;

public record CreateCollectionCommand(String name,
                                      String description,
                                      List<String> tags) {
}
