package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardNotFoundException;
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

        @Test
        @DisplayName("findAllByOwnerIdAndNameContainsIgnoreCase return Deck when Owner and Deck exist with filter")
        void findAllByOwnerIdAndNameContainsIgnoreCase_shouldReturnDecks_whenDecksAndOwnerExist() {
            // --- Given
            User otherUser = User.createNew(new Username("otherUser"));
            userRepository.save(otherUser);

            Deck deck2 = Deck.createNew("ThEkEy_start", "description 2", testUser.userId());
            Deck deck3 = Deck.createNew("end_thekey", "description 3", testUser.userId());
            Deck deck4 = Deck.createNew("middle_THEKEY_middle", "description 4", testUser.userId());
            Deck deck5 = Deck.createNew("Deck5", "description 5", otherUser.userId()); // Different owner

            deckRepository.save(deck2);
            deckRepository.save(deck3);
            deckRepository.save(deck4);
            deckRepository.save(deck5);

            // When
            List<Deck> foundDecks =
                    deckRepository.findAllByOwnerIdAndNameContainsIgnoreCase(testUser.userId(), "tHeKeY");
            assertThat(foundDecks).hasSize(3).containsExactlyInAnyOrder(deck2, deck3, deck4);
        }
    }

    @Nested
    @DisplayName("Flashcard operations")
    class FlashcardOperations {
        Flashcard testFlashcard = Flashcard.createNew(new Question("Question"), new Answer("Answer"));

        @BeforeEach
        void setUp() {
            userRepository.save(testUser);
            deckRepository.save(testDeck);
            flashcardRepository.save(testFlashcard, testUser.userId());
        }

        @Test
        @DisplayName("addFlashcardToDeck should succeed when Deck and Flashcard exist")
        void addFlashcardToDeck_shouldSucceed_whenDeckAndFlashcardExist() {
            // Given & When
            deckRepository.addFlashcardToDeck(testDeck.deckId(), testFlashcard.flashcardId(), testUser.userId());
            Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(testDeck.deckId(), testUser.userId());

            // Then
            assertThat(foundDeck).isPresent().get().satisfies(d -> {
                assertThat(d.flashcardIds()).contains(testFlashcard.flashcardId());
                assertThat(d.flashcardIds()).hasSize(1);
            });
        }

        @Test
        @DisplayName("addFlashcardToDeck should throw when Deck doesn't exist")
        void addFlashcardToDeck_shouldThrow_whenDeckDoesNotExist() {
            // Given
            DeckId randomDeckId = DeckId.generate();
            FlashcardId flashcardId = testFlashcard.flashcardId();
            UserId ownerId = testUser.userId();

            // When & Then
            assertThatThrownBy(() -> deckRepository.addFlashcardToDeck(randomDeckId, flashcardId, ownerId))
                    .isInstanceOf(DeckNotFoundException.class);
        }

        @Test
        @DisplayName("addFlashcardToDeck should throw when Deck doesn't exist")
        void addFlashcardToDeck_shouldThrow_whenFlashcardDoesNotExist() {
            // Given
            DeckId deckId = testDeck.deckId();
            FlashcardId randomFlashcardId = FlashcardId.generate();
            UserId ownerId = testUser.userId();

            // When & Then
            assertThatThrownBy(() -> deckRepository.addFlashcardToDeck(deckId, randomFlashcardId, ownerId))
                    .isInstanceOf(FlashcardNotFoundException.class);
        }
    }
}
