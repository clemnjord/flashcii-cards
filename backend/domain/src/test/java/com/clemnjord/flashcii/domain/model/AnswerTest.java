package com.clemnjord.flashcii.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AnswerTest {
    @Test
    void shouldThrowIllegalExceptionWhenAnswerIsBlank(){
        Assertions.assertThatThrownBy(() -> new Answer(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Answer value cannot be blank");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAnswerIsNull(){
        Assertions.assertThatThrownBy(() -> new Answer(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Answer value cannot be null");
    }
}
