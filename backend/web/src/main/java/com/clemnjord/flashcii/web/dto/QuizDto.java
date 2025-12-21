package com.clemnjord.flashcii.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class QuizDto {
    public record QuizResponse(
            @NotNull String uuid, @NotEmpty List<String> flashcardIds) {}

    public record CreateQuizRequest(@NotEmpty List<String> deckIds) {}

    public record RateFlashcardRequest(
            @NotNull String flashcardUuid, @NotNull String rating) {}
}
