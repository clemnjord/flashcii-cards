package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(PostgresTestContainerExtension.class)
@Transactional
class JpaQuizRepositoryTest {
    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Autowired
    JpaFixedQuizRepository quizRepository;

    private final User testUser = User.createNew(new Username("testUser"));
    Flashcard flashcard1 = new Flashcard(
            FlashcardId.from("a1234567-6ea0-4ebc-a0c9-a017a85b97ec"), new Question("Question1"), new Answer("Answer1"));
    Flashcard flashcard2 = new Flashcard(
            FlashcardId.from("b8910111-6ea0-4ebc-a0c9-a017a85b97ec"), new Question("Question2"), new Answer("Answer2"));

    @BeforeEach
    void setUpEach() {
        userRepository.save(testUser);
        flashcardRepository.save(flashcard1, testUser.userId());
        flashcardRepository.save(flashcard2, testUser.userId());
    }

    @Nested
    @DisplayName("Save operations")
    class SaveOperations {

        @Test
        @DisplayName("Should persist Quiz when user exists and flashcards exist")
        void save_shouldPersistQuiz(@Autowired EntityManager entityManager, @Autowired DataSource dataSource) {
            // Given
            FixedSizedQuiz quiz = FixedSizedQuiz.createNew(
                    testUser.userId(), List.of(flashcard1.flashcardId(), flashcard2.flashcardId()));
            quiz.answerCurrentQuestion(quiz.getCurrentFlashcardId().get());

            // When
            quizRepository.save(quiz, testUser.userId());
            entityManager.flush();

            // Then
            var dsWrapper = new TransactionAwareDataSourceProxy(dataSource);
            var assertDbConnection = AssertDbConnectionFactory.of(dsWrapper).create();

            Table quizzesTable = assertDbConnection.table("quizzes").build();

            Assertions.assertThat(quizzesTable)
                    .row()
                    .column("quiz_id")
                    .value()
                    .isEqualTo(quiz.getId().uuid());
            Assertions.assertThat(quizzesTable)
                    .row()
                    .column("owner_id")
                    .value()
                    .isEqualTo(testUser.userId().uuid());
            Assertions.assertThat(quizzesTable)
                    .row()
                    .column("current_question_index")
                    .value()
                    .isEqualTo(quiz.getCurrentQuestionIndex());

            Table quizFlashcardsTable =
                    assertDbConnection.table("quiz_flashcards").build();
            Assertions.assertThat(quizFlashcardsTable).hasNumberOfRows(2);
            Assertions.assertThat(quizFlashcardsTable)
                    .row()
                    .column("flashcard_id")
                    .value()
                    .isEqualTo(flashcard1.flashcardId().uuid());
            Assertions.assertThat(quizFlashcardsTable)
                    .row()
                    .column("owner_id")
                    .value()
                    .isEqualTo(testUser.userId().uuid());
            Assertions.assertThat(quizFlashcardsTable)
                    .row()
                    .column("quiz_id")
                    .value()
                    .isEqualTo(quiz.getId().uuid());
        }

        @Test
        @DisplayName("Should throw when flashcard does not exist")
        void save_shouldThrow_whenFlashcardDoesNotExist() {
            // Given
            FixedSizedQuiz quiz = FixedSizedQuiz.createNew(testUser.userId(), List.of(FlashcardId.generate()));
            var ownerId = testUser.userId();

            // When
            assertThatThrownBy(() -> quizRepository.save(quiz, ownerId)).isInstanceOf(FlashcardNotFoundException.class);
        }

        @Test
        @DisplayName("Should throw when owner does not exist")
        void save_shouldThrow_whenOwnerDoesNotExist() {
            // Given
            FixedSizedQuiz quiz = FixedSizedQuiz.createNew(testUser.userId(), List.of(FlashcardId.generate()));
            var randomOwnerId = UserId.generate();

            // When
            assertThatThrownBy(() -> quizRepository.save(quiz, randomOwnerId))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Find operations")
    class FindOperations {
        @Test
        @DisplayName("findById should return quiz when it exists")
        void findById_shouldReturnQuiz_whenQuizAndOwnerExist() {
            // --- Given
            FixedSizedQuiz quiz = FixedSizedQuiz.createNew(
                    testUser.userId(), List.of(flashcard1.flashcardId(), flashcard2.flashcardId()));
            quizRepository.save(quiz, testUser.userId());

            // --- When
            Optional<FixedSizedQuiz> retrievedQuiz = quizRepository.findById(quiz.getId());

            // Then
            assertThat(retrievedQuiz)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(quiz);
        }

        @Test
        @DisplayName("findById should return empty when it doesn't exist")
        void findById_shouldReturnEmpty_whenDoesNotExist() {
            // --- Given
            var randomQuizId = QuizId.generate();

            // --- When
            Optional<FixedSizedQuiz> retrievedQuiz = quizRepository.findById(randomQuizId);

            // Then
            assertThat(retrievedQuiz).isEmpty();
        }
    }
}
