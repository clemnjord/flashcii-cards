package com.clemnjord.flashcii.application.flashcard.command;

import java.util.List;

public record CreateCollectionCommand(String name, String description, List<String> tags) {}
