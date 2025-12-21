package com.clemnjord.flashcii.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class QuizDto {
    public record QuizResponse(String uuid, List<String> flashcardIds) {}

    public record CreateQuizRequest(@NotNull @NotEmpty List<String> deckIds) {}

    public record RateFlashcardRequest(
            @NotBlank(message = "Flashcard UUID can't be blank") String flashcardUuid,

            @NotNull(message = "Rating can't be blank") String rating) {}
}
