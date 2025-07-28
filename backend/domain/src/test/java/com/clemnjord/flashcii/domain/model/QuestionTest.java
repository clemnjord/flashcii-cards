package com.clemnjord.flashcii.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuestionTest {
    @Test
    void shouldThrowIllegalExceptionWhenQuestionIsBlank(){
        Assertions.assertThatThrownBy(() -> new Question(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Question value cannot be blank");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenQuestionIsNull(){
        Assertions.assertThatThrownBy(() -> new Question(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Question value cannot be null");
    }

    @Test
    void shouldThrowIllegalExceptionWhenQuestionIsTooLong(){
        String longQuestion = "a".repeat(501);
        Assertions.assertThatThrownBy(() -> new Question(longQuestion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Question cannot be longer than 500 characters");
    }
}
