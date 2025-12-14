package com.clemnjord.flashcii.web.dto;

import java.util.List;

public class QuizDto {
    public record QuizResponse(String uuid, List<String> flashcardIds) {}

    public record CreateQuizRequest(List<String> deckIds, int quizSize) {}

    public record RateFlashcardRequest(String quizUuid, String flashcardUuid, String rating) {}
}
