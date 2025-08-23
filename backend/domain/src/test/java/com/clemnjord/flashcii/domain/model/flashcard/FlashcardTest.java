package com.clemnjord.flashcii.domain.model.flashcard;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void restoredFlashcardEqualsOriginalFlashcard() {
        // --- Arrange & Act
        Flashcard originalFlashcard = Flashcard.createNew(new Question("A question"), new Answer("An answer"));
        Flashcard restoredFlashcard = Flashcard.restore(originalFlashcard.flashcardId(), originalFlashcard.question(), originalFlashcard.answer());

        // --- Assert
        assertThat(restoredFlashcard).isEqualTo(originalFlashcard);
    }

}
