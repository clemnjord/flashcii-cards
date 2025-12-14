package com.clemnjord.flashcii.domain.model.quiz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FixedSizedQuizTest {
    QuizId testQuizId = QuizId.generate();
    FlashcardId testFlashcardId = FlashcardId.generate();
    List<FlashcardId> testFlashcardIds = List.of(testFlashcardId);

    @Test
    @DisplayName("Test that Quiz can be instantiated with valid parameters")
    void shouldInstantiateFixedSizeQuiz_whenParametersAreValid() {
        FixedSizedQuiz fixedSizedQuiz = new FixedSizedQuiz(testQuizId, testFlashcardIds);

        assertThat(fixedSizedQuiz).isNotNull();
        assertThat(fixedSizedQuiz.getId()).isEqualTo(testQuizId);
        assertThat(fixedSizedQuiz.getFlashcardIds()).isEqualTo(testFlashcardIds);
    }

    @Test
    @DisplayName("Test that Quiz can be created with valid parameters")
    void shouldCreateNewFixedSizeQuiz_whenParametersAreValid() {
        FixedSizedQuiz fixedSizedQuiz = FixedSizedQuiz.createNew(testFlashcardIds);

        assertThat(fixedSizedQuiz).isNotNull();
        assertThat(fixedSizedQuiz.getId()).isNotNull();
        assertThat(fixedSizedQuiz.getFlashcardIds()).isEqualTo(testFlashcardIds);
    }

    @Test
    @DisplayName("Test that Quiz Id can't be null")
    void shouldThrow_whenQuizIdIsNull() {
        assertThatThrownBy(() -> new FixedSizedQuiz(null, testFlashcardIds)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Test that FlashcardIds can't be null")
    void shouldThrow_whenFlashcardIdsIsNull() {
        assertThatThrownBy(() -> new FixedSizedQuiz(testQuizId, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Test that FlashcardIds can't be empty")
    void shouldThrow_whenFlashcardIdsIsEmpty() {
        List<FlashcardId> emptyList = List.of();
        assertThatThrownBy(() -> new FixedSizedQuiz(testQuizId, emptyList))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Test that FixedSizeQuiz is finished when there are no more cards")
    void isFinishedShouldReturnFalse_whenThereAreStillCards() {
        FixedSizedQuiz fixedSizedQuiz = new FixedSizedQuiz(testQuizId, List.of(testFlashcardId));
        assertThat(fixedSizedQuiz.isFinished()).isFalse();
    }

    @Test
    @DisplayName("Test that FixedSizeQuiz is finished when there are no more cards")
    void isFinishedShouldReturnTrue_whenThereAreNoMoreCards() {
        FixedSizedQuiz fixedSizedQuiz = new FixedSizedQuiz(testQuizId, List.of(testFlashcardId));
        fixedSizedQuiz.answerCurrentQuestion(testFlashcardId);
        assertThat(fixedSizedQuiz.isFinished()).isTrue();
    }

    @Test
    @DisplayName("Test getting current FlashcardId when quiz is finished")
    void getCurrentFlashcardIdShouldReturnCurrentFlashcardId_whenFinished() {
        FixedSizedQuiz fixedSizedQuiz = new FixedSizedQuiz(testQuizId, List.of(testFlashcardId));
        fixedSizedQuiz.answerCurrentQuestion(testFlashcardId);
        assertThat(fixedSizedQuiz.getCurrentFlashcardId()).isEmpty();
    }

    // TODO: Add test for answerCurrentQuestion with mismatched FlashcardId

    @Test
    @DisplayName("Test getting current FlashcardId when quiz is not finished")
    void getCurrentFlashcardIdShouldReturnCurrentFlashcardId_whenNotFinished() {
        FixedSizedQuiz fixedSizedQuiz = new FixedSizedQuiz(testQuizId, List.of(testFlashcardId));
        assertThat(fixedSizedQuiz.getCurrentFlashcardId()).isPresent().contains(testFlashcardId);
    }

    @Test
    @DisplayName("Test Quiz equality")
    void testQuizEquality() {
        EqualsVerifier.simple().forClass(FixedSizedQuiz.class).verify();
    }
}
