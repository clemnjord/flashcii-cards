package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfiguration.class)
@ActiveProfiles("test")
class JpaDeckRepositoryTest {

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Autowired
    private JpaDeckRepository deckRepository;

    @Test
    void save_shouldPersistDeck() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());

        // When
        deckRepository.save(deck);
        Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(deck.deckId(), user.userId());

        // Then
        assertThat(foundDeck).isPresent();
        assertThat(foundDeck.get().deckId()).isEqualTo(deck.deckId());
        assertThat(foundDeck.get().name()).isEqualTo(deck.name());
        assertThat(foundDeck.get().description()).isEqualTo(deck.description());
    }

    @Test
    void save_shouldThrow_whenUserDoesNotExist() {
        // Given
        UserId randomUserId = UserId.generate();
        Deck deck = Deck.createNew("DeckName", "description", randomUserId);

        // When & Then
        assertThatThrownBy(() -> deckRepository.save(deck))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found: " + randomUserId);
    }

    @Test
    void existsByNameAndOwnerId_shouldReturnTrue_whenExists() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        // When
        boolean exists = deckRepository.existsByNameAndOwnerId(deck.name(), user.userId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByIdAndOwnerId_shouldReturnTrue_whenExists() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        // When
        boolean exists = deckRepository.existsByIdAndOwnerId(deck.deckId(), user.userId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void findByNameAndOwnerId_shouldReturnDeck_whenExists() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        // When
        Optional<Deck> foundDeck = deckRepository.findByNameAndOwnerId(deck.name(), user.userId());
        assertThat(foundDeck).isPresent();
        assertThat(foundDeck.get().deckId()).isEqualTo(deck.deckId());
        assertThat(foundDeck.get().name()).isEqualTo(deck.name());
        assertThat(foundDeck.get().description()).isEqualTo(deck.description());
    }

    @Test
    void findByNameAndOwnerId_shouldReturnEmptyDeck_whenDoesntExists() {
        // When
        Optional<Deck> foundDeck = deckRepository.findByNameAndOwnerId("doesNotExist", UserId.generate());
        assertThat(foundDeck).isEmpty();
    }

    @Test
    void findByIdAndOwnerId_shouldReturnDeck_whenExists() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        // When
        Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(deck.deckId(), user.userId());
        assertThat(foundDeck).isPresent();
        assertThat(foundDeck.get().deckId()).isEqualTo(deck.deckId());
        assertThat(foundDeck.get().name()).isEqualTo(deck.name());
        assertThat(foundDeck.get().description()).isEqualTo(deck.description());
    }

    @Test
    void findByIdAndOwnerId_shouldReturnEmptyDeck_whenDoesntExists() {
        // When
        Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(DeckId.generate(), UserId.generate());
        assertThat(foundDeck).isEmpty();
    }

    @Test
    void addFlashcardToDeck_shouldSucceed_whenDeckAndFlashcardExist() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.createNew(new Question("Question"), new Answer("Answer"));
        flashcardRepository.save(flashcard, user.userId());

        // When
        deckRepository.addFlashcardToDeck(deck.deckId(), flashcard.flashcardId(), user.userId());
        Optional<Deck> foundDeck = deckRepository.findByIdAndOwnerId(deck.deckId(), user.userId());

        // Then
        assertThat(foundDeck).isPresent();
        assertThat(foundDeck.get().flashcardIds()).contains(flashcard.flashcardId());
        assertThat(foundDeck.get().flashcardIds()).hasSize(1);
    }
}
