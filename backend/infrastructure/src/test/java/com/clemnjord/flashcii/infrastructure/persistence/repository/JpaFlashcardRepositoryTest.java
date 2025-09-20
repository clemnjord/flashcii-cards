package com.clemnjord.flashcii.infrastructure.persistence.repository;

import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfiguration.class)
@ActiveProfiles("test")
class JpaFlashcardRepositoryTest {

    @Autowired
    JpaUserRepository userRepository;
    @Autowired
    private JpaFlashcardRepository flashcardRepository;
    @Autowired
    private JpaDeckRepository deckRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveFlashcard() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        // When
        flashcardRepository.save(flashcard, deck.deckId(),  user.userId());
        Optional<Flashcard> foundFlashcard = flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), user.userId());

        // Then
        assertThat(foundFlashcard).isPresent();
        assertThat(foundFlashcard.get().question().value()).isEqualTo(flashcard.question().value());
        assertThat(foundFlashcard.get().answer().value()).isEqualTo(flashcard.answer().value());
        assertThat(foundFlashcard.get().flashcardId()).isEqualTo(flashcard.flashcardId());
    }

    @Test
    void saveShouldThrowWhenDeckDoesNotExist() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Flashcard flashcard = Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        DeckId randomDeckId = DeckId.generate();
        UserId userId = user.userId();

        // When & Then
        assertThatThrownBy(() -> flashcardRepository.save(flashcard, randomDeckId, userId))
                .isInstanceOf(DeckNotFoundException.class)
                .hasMessageContaining("Deck not found when saving a Flashcard");
    }

    @Test
    void saveShouldThrowWhenUserDoesNotExist() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        DeckId deckId = deck.deckId();
        UserId randomUserId = UserId.generate();

        // When & Then
        assertThatThrownBy(() -> flashcardRepository.save(flashcard, deckId, randomUserId))
                .isInstanceOf(DeckNotFoundException.class)
                .hasMessageContaining("Deck not found when saving a Flashcard");
    }
}