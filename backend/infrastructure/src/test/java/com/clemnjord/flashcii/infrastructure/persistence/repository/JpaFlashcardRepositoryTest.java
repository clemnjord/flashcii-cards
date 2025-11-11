package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.domain.exception.user.UserNotFoundException;
import com.clemnjord.flashcii.domain.model.deck.Deck;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

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

    @Test
    void saveFlashcard() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        // When
        flashcardRepository.save(flashcard, user.userId());
        Optional<Flashcard> foundFlashcard =
                flashcardRepository.findByFlashcardIdAndOwnerId(flashcard.flashcardId(), user.userId());

        // Then
        assertThat(foundFlashcard).isPresent();
        assertThat(foundFlashcard.get().question().value())
                .isEqualTo(flashcard.question().value());
        assertThat(foundFlashcard.get().answer().value())
                .isEqualTo(flashcard.answer().value());
        assertThat(foundFlashcard.get().flashcardId()).isEqualTo(flashcard.flashcardId());
    }

    @Test
    void saveShouldThrowWhenUserDoesNotExist() {
        // Given
        User user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        Deck deck = Deck.createNew("DeckName", "description", user.userId());
        deckRepository.save(deck);

        Flashcard flashcard = Flashcard.createNew(new Question("What is a question"), new Answer("An answer."));

        UserId randomUserId = UserId.generate();

        // When & Then
        assertThatThrownBy(() -> flashcardRepository.save(flashcard, randomUserId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found when saving a Flashcard");
    }
}
