package com.clemnjord.flashcii.web.dto;

import java.util.List;

public class QuizDto {
    public record QuizResponse(String uuid, List<String> flashcardIds) {}

    public record CreateQuizRequest(List<String> deckIds) {}

    public record RateFlashcardRequest(String flashcardUuid, String rating) {}
}
