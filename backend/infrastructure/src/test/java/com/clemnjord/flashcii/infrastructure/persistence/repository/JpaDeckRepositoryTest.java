package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import jakarta.persistence.EntityManager;
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
class JpaDeckRepositoryTest {
    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Autowired
    private JpaDeckRepository deckRepository;

    private final User testUser = User.createNew(new Username("testUser"));
    private final Deck testDeck = Deck.createNew("DeckName", "description", testUser.userId());

    @Nested
    @DisplayName("Save operations")
    class SaveOperations {

        @Test
        @DisplayName("Should persist Deck when user exists")
        void save_shouldPersistDeck(@Autowired EntityManager entityManager, @Autowired DataSource dataSource) {
            // Given
            User user = User.createNew(new Username("testUser"));
            userRepository.save(user);

            Deck deck = Deck.createNew("DeckName", "description", user.userId());

            // When
            deckRepository.save(deck);
            entityManager.flush();

            // Then
            var dsWrapper = new TransactionAwareDataSourceProxy(dataSource);
            var assertDbConnection = AssertDbConnectionFactory.of(dsWrapper).create();
            Table decksTable = assertDbConnection.table("decks").build();

            Assertions.assertThat(decksTable)
                    .row()
                    .column("id")
                    .value()
                    .isEqualTo(deck.deckId().uuid());
            Assertions.assertThat(decksTable)
                    .row()
                    .column("owner_id")
                    .value()
                    .isEqualTo(user.userId().uuid());
            Assertions.assertThat(decksTable).row().column("name").value().isEqualTo("DeckName");
            Assertions.assertThat(decksTable)
                    .row()
                    .column("description")
                    .value()
                    .isEqualTo("description");
        }

        @Test
        @DisplayName("Should throw when user does not exist")
        void save_shouldThrow_whenUserDoesNotExist() {
            // Given
            UserId randomUserId = UserId.generate();
            Deck deck = Deck.createNew("DeckName", "description", randomUserId);

            // When & Then
            assertThatThrownBy(() -> deckRepository.save(deck))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found: " + randomUserId);
        }
    }

    @Nested
    @DisplayName("Find operations")
    class FindOperations {

        @BeforeEach
        void setUp() {
            userRepository.save(testUser);
            deckRepository.save(testDeck);
        }

        @Test
        @DisplayName("existsByNameAndOwnerId Should return true when Deck and Owner exist")
        void existsByNameAndOwnerId_shouldReturnTrue_whenExists() {
            // When
            boolean exists = deckRepository.existsByNameAndOwnerId(testDeck.name(), testUser.userId());

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("existsByIdAndOwnerId should return true when Deck and Owner exist")
        void existsByIdAndOwnerId_shouldReturnTrue_whenExists() {
            // When
            boolean exists = deckRepository.existsByIdAndOwnerId(testDeck.deckId(), testUser.userId());

            // Then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("findByNameAndOwnerId should return Deck when Deck and User exist")
        void findByNameAndOwnerId_shouldReturnDeck_whenExists() {
            // When
            Optional<Deck> foundDeck = deckRepository.findByNameAndOwnerId(testDeck.name(), testUser.userId());
            assertThat(foundDeck).isPresent().get().satisfies(d -> {
                assertThat(d.deckId()).isEqualTo(testDeck.deckId());
                assertThat(d.name()).isEqualTo(testDeck.name());
                assertThat(d.description()).isEqualTo(testDeck.description());
            });
        }

        @Test
        @DisplayName("findByNameAndOwnerId should return empty when Deck does not exist")
        void findByNameAndOwnerId_shouldReturnEmptyDeck_whenDoesntExists() {
            // When
            Optional<Deck> foundDeck = deckRepository.findByNameAndOwnerId("doesNotExist", UserId.generate());
            assertThat(foundDeck).isEmpty();
        }

        @Test
        @DisplayName("findByIdAndOwnerId should return Deck when Deck and User exist")
        void findByIdAndOwnerId_shouldReturnDeck_whenExists() {
            // When
            Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(testDeck.deckId(), testUser.userId());
            assertThat(foundDeck).isPresent().get().satisfies(d -> {
                assertThat(d.deckId()).isEqualTo(testDeck.deckId());
                assertThat(d.name()).isEqualTo(testDeck.name());
                assertThat(d.description()).isEqualTo(testDeck.description());
            });
        }

        @Test
        @DisplayName("findByIdAndOwnerId should return empty when Deck does not exist")
        void findByIdAndOwnerId_shouldReturnEmptyDeck_whenDoesntExists() {
            // When
            Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(DeckId.generate(), UserId.generate());
            assertThat(foundDeck).isEmpty();
        }
    }

    @Nested
    @DisplayName("Flashcard operations")
    class FlashcardOperations {

        @BeforeEach
        void setUp() {
            userRepository.save(testUser);
            deckRepository.save(testDeck);
        }

        @Test
        @DisplayName("addFlashcardToDeck should succeed when Deck and Flashcard exist")
        void addFlashcardToDeck_shouldSucceed_whenDeckAndFlashcardExist() {
            // Given
            Flashcard flashcard = Flashcard.createNew(new Question("Question"), new Answer("Answer"));
            flashcardRepository.save(flashcard, testUser.userId());

            // When
            deckRepository.addFlashcardToDeck(testDeck.deckId(), flashcard.flashcardId(), testUser.userId());
            Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(testDeck.deckId(), testUser.userId());

            // Then
            assertThat(foundDeck).isPresent().get().satisfies(d -> {
                assertThat(d.flashcardIds()).contains(flashcard.flashcardId());
                assertThat(d.flashcardIds()).hasSize(1);
            });
        }
    }
}
