package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Table;
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
@ExtendWith(PostgresTestContainerExtension.class)
@ActiveProfiles("test")
@Transactional
class JpaFlashcardRepositoryTest {

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Nested
    @DisplayName("Save operations")
    class SaveOperations {
        private final User user = User.createNew(new Username("testUser"));
        private final Flashcard flashcard =
                Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        @Test
        @DisplayName("Should persist Flashcard when user exists")
        void save_shouldPersistFlashcard(@Autowired EntityManager entityManager, @Autowired DataSource dataSource) {
            // Given
            userRepository.save(user);

            // When
            flashcardRepository.save(flashcard, user.userId());
            entityManager.flush();

            // Then
            var dsWrapper = new TransactionAwareDataSourceProxy(dataSource);
            var assertDbConnection = AssertDbConnectionFactory.of(dsWrapper).create();
            Table flashcardsTable = assertDbConnection.table("flashcards").build();

            Assertions.assertThat(flashcardsTable)
                    .row()
                    .column("flashcard_id")
                    .value()
                    .isEqualTo(flashcard.flashcardId().uuid());
            Assertions.assertThat(flashcardsTable)
                    .row()
                    .column("owner_id")
                    .value()
                    .isEqualTo(user.userId().uuid());
            Assertions.assertThat(flashcardsTable)
                    .row()
                    .column("question")
                    .value()
                    .isEqualTo(flashcard.question().value());
            Assertions.assertThat(flashcardsTable)
                    .row()
                    .column("answer")
                    .value()
                    .isEqualTo(flashcard.answer().value());
        }

        @Test
        @DisplayName("Should throw when user does not exist")
        void saveShouldThrowWhenUserDoesNotExist() {
            // Given
            UserId randomUserId = UserId.generate();

            // When & Then
            assertThatThrownBy(() -> flashcardRepository.save(flashcard, randomUserId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found when saving a Flashcard");
        }
    }
}
