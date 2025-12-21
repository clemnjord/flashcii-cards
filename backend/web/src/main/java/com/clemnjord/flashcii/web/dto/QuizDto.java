package com.clemnjord.flashcii.web.dto;

import io.github.openspacedrepetition.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class QuizDto {
    public record QuizResponse(
            String uuid, List<String> flashcardIds) {}

    public record CreateQuizRequest(@NotNull @NotEmpty List<String> deckIds) {
    }

    public record RateFlashcardRequest(
            @NotBlank(message = "Flashcard UUID can't be blank") String flashcardUuid,
            @NotNull(message = "Rating can't be blank") String rating) {

        public RateFlashcardRequest {
            // Validate that flashcardUuid is a valid UUID
            UUID.fromString(flashcardUuid);

            // Validate that rating is one of the allowed values
            Rating.valueOf(rating);
        }
    }
}
