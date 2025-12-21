package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import io.github.openspacedrepetition.Card;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Set;
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
    private JpaDeckRepository deckRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Autowired
    private JpaFlashcardStatisticRepository flashcardStatisticRepository;

    private final User user = User.createNew(new Username("testUser"));
    private final Flashcard flashcard = Flashcard.createNew(new Question("A question."), new Answer("An answer."));

    @Nested
    @DisplayName("Save operations")
    class SaveOperations {

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

    @Nested
    @DisplayName("Find operations")
    class FindOperations {

        @Test
        @DisplayName("Should find Flashcard by FlashcardId and OwnerId when it exists")
        void findByFlashcardIdAndOwnerId_shouldReturnFlashcard_whenItExists(@Autowired EntityManager entityManager) {
            // Given
            userRepository.save(user);
            flashcardRepository.save(flashcard, user.userId());
            entityManager.flush();

            // When
            var retrievedFlashcard =
                    flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), user.userId());

            // Then
            assertThat(retrievedFlashcard).isPresent().get().isEqualTo(flashcard);
        }

        @Test
        @DisplayName("Should not find any Flashcard by FlashcardId and OwnerId, when Flashcard does not exist")
        void findByFlashcardIdAndOwnerId_shouldReturnEmpty_whenFlashcardIdDoesNotExist(
                @Autowired EntityManager entityManager) {
            // Given
            userRepository.save(user);
            entityManager.flush();

            // When
            var retrievedFlashcard =
                    flashcardRepository.findByFlashcardIdAndOwnerId(FlashcardId.generate(), user.userId());

            // Then
            assertThat(retrievedFlashcard).isEmpty();
        }

        @Test
        @DisplayName("Should not find any Flashcard by FlashcardId and OwnerId, when Owner does not exist")
        void findByFlashcardIdAndOwnerId_shouldReturnEmpty_whenOwnerDoesNotExist(
                @Autowired EntityManager entityManager) {
            // Given
            userRepository.save(user);
            flashcardRepository.save(flashcard, user.userId());
            entityManager.flush();

            // When
            var retrievedFlashcard =
                    flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), UserId.generate());

            // Then
            assertThat(retrievedFlashcard).isEmpty();
        }

        @Test
        @DisplayName("Should find due Flashcard across two decks")
        void findDueFlashcardsByDeckAndOwner_shouldReturnDueFlashcard_whenTheyExist(
                @Autowired EntityManager entityManager) {
            // Given
            Flashcard flashcard2 = Flashcard.createNew(new Question("A question."), new Answer("An answer."));
            Flashcard flashcard3 = Flashcard.createNew(new Question("A question."), new Answer("An answer."));

            Deck deck1 = new Deck(
                    DeckId.generate(),
                    "Sample Deck",
                    "A deck for testing",
                    user.userId(),
                    Set.of(flashcard.flashcardId()));
            Deck deck2 = Deck.createNew("Sample Deck2", "A deck for testing", user.userId());

            userRepository.save(user);
            flashcardRepository.save(flashcard, user.userId());
            flashcardRepository.save(flashcard2, user.userId());
            flashcardRepository.save(flashcard3, user.userId());

            // Flashcard 1 due, flashcard 2 not due, flashcard 3 no statistic (considered due)
            flashcardStatisticRepository.save(
                    flashcard.flashcardId(),
                    user.userId(),
                    Card.builder().due(Instant.now().minusSeconds(3600)).build());
            flashcardStatisticRepository.save(
                    flashcard2.flashcardId(),
                    user.userId(),
                    Card.builder().due(Instant.now().plusSeconds(3600)).build());
            deckRepository.save(deck1);
            deckRepository.save(deck2);
            // Flashcard 1 in Deck 1 and Deck 2, Flashcard 2 and 3 in Deck 2
            deckRepository.addFlashcardToDeck(deck1.deckId(), flashcard.flashcardId(), user.userId());
            deckRepository.addFlashcardToDeck(deck2.deckId(), flashcard.flashcardId(), user.userId());
            deckRepository.addFlashcardToDeck(deck2.deckId(), flashcard2.flashcardId(), user.userId());
            deckRepository.addFlashcardToDeck(deck2.deckId(), flashcard3.flashcardId(), user.userId());
            entityManager.flush();

            // When
            var retrievedFlashcards = flashcardRepository.findDueFlashcardsByDeckIdsAndOwnerId(
                    Set.of(deck1.deckId(), deck2.deckId()), user.userId());

            // Then
            assertThat(retrievedFlashcards).hasSize(2);
        }

        @Test
        @DisplayName("Should find no due Flashcard")
        void findDueFlashcardsByDeckAndOwner_shouldEmpty_whenNoFlashcardAreDue(@Autowired EntityManager entityManager) {
            // Given
            Deck deck1 = Deck.createNew("Sample Deck", "A deck for testing", user.userId());

            userRepository.save(user);
            flashcardRepository.save(flashcard, user.userId());
            flashcardStatisticRepository.save(
                    flashcard.flashcardId(),
                    user.userId(),
                    Card.builder().due(Instant.now().plusSeconds(3600)).build());
            deckRepository.save(deck1);
            deckRepository.addFlashcardToDeck(deck1.deckId(), flashcard.flashcardId(), user.userId());
            entityManager.flush();

            // When
            var retrievedFlashcards =
                    flashcardRepository.findDueFlashcardsByDeckIdsAndOwnerId(Set.of(deck1.deckId()), user.userId());

            // Then
            assertThat(retrievedFlashcards).isEmpty();
        }
    }
}
