package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import java.util.Optional;

public interface IFixedSizedQuizRepository {

    void save(FixedSizedQuiz quiz, UserId ownerId);

    Optional<FixedSizedQuiz> findById(QuizId quizId);
}
