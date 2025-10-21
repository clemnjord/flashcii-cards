package com.clemnjord.flashcii.web.dto;

import jakarta.validation.constraints.NotBlank;

public class FlashcardDto {
    public record FlashcardCreateRequest(
            @NotBlank(message = "User ID is required") String userId,

            @NotBlank(message = "Question is required") String question,

            @NotBlank(message = "Answer is required") String answer) {}

    public record FlashcardResponse(String uuid, String question, String answer) {
    }

}
