package com.clemnjord.flashcii.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class DeckDto {
    public record DeckRequest(
            @NotBlank(message = "Name is required")
                    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
                    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_]+$", message = "Name contains invalid characters")
                    String name,
            @Size(max = 500, message = "Description must not exceed 500 characters") String description) {}

    public record DeckResponse(String uuid, String name, String description, List<String> flashcardIds) {}

    public record SimpleDeckResponse(String uuid, String name, String description) {}

    public record AddFlashcardToDeckRequest(@NotBlank(message = "Flashcard ID is required") String flashcardId) {}
}
