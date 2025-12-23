package com.clemnjord.flashcii.domain.model.flashcard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class FlashcardTest {

    @Test
    void createFlashcardWhenInputIsValid() {
        // Arrange
        Question question = new Question("What is the capital of France?");
        Answer answer = new Answer("Paris");

        // Act
        Flashcard flashcard = Flashcard.createNew(question, answer);

        // Assert
        assertThat(flashcard).isNotNull();
        assertThat(flashcard.flashcardId()).isNotNull();
        assertThat(flashcard.question().value()).isEqualTo("What is the capital of France?");
        assertThat(flashcard.answer().value()).isEqualTo("Paris");
    }

    @Test
    void throwExceptionWhenQuestionIsNull() {
        // Arrange
        Answer answer = new Answer("Paris");

        // Act & Assert
        assertThatThrownBy(() -> Flashcard.createNew(null, answer))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Question cannot be null");
    }

    @Test
    void throwExceptionWhenAnswerIsNull() {
        // Arrange
        Question question = new Question("What is the capital of France?");

        // Act & Assert
        assertThatThrownBy(() -> Flashcard.createNew(question, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Answer cannot be null");
    }

    @Test
    void flashcardEquality() {
        EqualsVerifier.forClass(Flashcard.class).suppress(Warning.NULL_FIELDS).verify();
    }
}
