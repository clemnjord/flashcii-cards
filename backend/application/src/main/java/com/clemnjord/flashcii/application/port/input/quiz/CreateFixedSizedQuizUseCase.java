package com.clemnjord.flashcii.application.port.input.quiz;

import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;

public interface CreateFixedSizedQuizUseCase {
    FixedSizedQuiz execute(CreateFixedSizedQuizCommand command);
}
