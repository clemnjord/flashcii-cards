package com.clemnjord.flashcii.application.port.input.flashcard;

import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import io.github.openspacedrepetition.Rating;

public record RateFlashcardCommand(QuizId quizId, FlashcardId flashcardId, Rating rating) {}
