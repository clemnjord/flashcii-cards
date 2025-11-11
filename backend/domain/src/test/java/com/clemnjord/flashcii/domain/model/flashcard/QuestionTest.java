package com.clemnjord.flashcii.domain.model.flashcard;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuestionTest {
    @Test
    void createQuestionWhenInputIsValid() {
        Question question = new Question("What is the question?");
        Assertions.assertThat(question.value()).isEqualTo("What is the question?");
    }

    @Test
    void throwExceptionWhenQuestionIsNull() {
        Assertions.assertThatThrownBy(() -> new Question(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Question value cannot be null");
    }

    @Test
    void throwExceptionWhenQuestionIsTooLong() {
        String longQuestion = "a".repeat(501);
        Assertions.assertThatThrownBy(() -> new Question(longQuestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Question cannot be longer than 500 characters");
    }

    @Test
    void throwExceptionWhenQuestionIsTooShort() {
        Assertions.assertThatThrownBy(() -> new Question("a"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Question cannot be shorter than 3 characters");
    }
}
