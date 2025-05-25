package com.clemnjord.flashcii.domain.quiz.model;

public record QuizConfiguration (int numberOfQuestions){
    public static final int DEFAULT_NUMBER_OF_QUESTIONS = 10;

    public QuizConfiguration() {
        this(DEFAULT_NUMBER_OF_QUESTIONS);
    }

    public QuizConfiguration {
        if (numberOfQuestions <= 0) {
            throw new IllegalArgumentException("Number of questions must be greater than zero.");
        }
    }
}
