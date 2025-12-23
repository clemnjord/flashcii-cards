package com.clemnjord.flashcii.domain.exception.quiz;

public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(String message) {
        super(message);
    }
}
