package com.clemnjord.flashcii.domain.model.deck;

import com.clemnjord.flashcii.domain.exception.deck.InvalidDeckException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.user.UserId;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeckTest {
    @Test
    void createNewDeckWhenInputIsValid() {
        // --- Arrange & Act
        Deck deck = Deck.createNew("Sample Deck", "A test deck", UserId.generate());

        // --- Assert
        assertThat(deck).isNotNull();
        assertThat(deck.deckId()).isNotNull();
        assertThat(deck.name()).isEqualTo("Sample Deck");
        assertThat(deck.description()).isEqualTo("A test deck");
        assertThat(deck.ownerId()).isNotNull();
        assertThat(deck.flashcardIds()).isEmpty();
    }

    @Test
    void hasEmptyDescriptionWhenNotProvided() {
        // --- Arrange & Act
        Deck deck = Deck.createNew("Sample Deck", null, UserId.generate());

        // --- Assert
        assertThat(deck.description()).isEmpty();
    }

    @Test
    void throwExceptionWhenNameIsNull() {
        // --- Arrange
        UserId ownerId = UserId.generate();

        // --- Act & Assert
        assertThatThrownBy(() -> Deck.createNew(null, "A test deck", ownerId))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Deck name is required");
    }

    @Test
    void throwExceptionWhenNameIsTooShort() {
        // --- Arrange
        UserId ownerId = UserId.generate();

        // --- Act & Assert
        assertThatThrownBy(() -> Deck.createNew("aa", "A test deck", ownerId))
                .isInstanceOf(InvalidDeckException.class)
                .hasMessageContaining("Deck name too short (min 3 characters)");
    }

    @Test
    void throwExceptionWhenNameIsTooLong() {
        // --- Arrange
        UserId ownerId = UserId.generate();
        String longName = "a".repeat(101);

        // --- Act & Assert
        assertThatThrownBy(() -> Deck.createNew(longName, "A test deck", ownerId))
                .isInstanceOf(InvalidDeckException.class)
                .hasMessageContaining("Deck name too long (max 100 characters)");
    }

    @Test
    void throwExceptionWhenDeckIdIsNull() {
        // --- Arrange
        UserId ownerId = UserId.generate();
        Set<FlashcardId> flashcardIds = Set.of();

        // --- Act & Assert
        assertThatThrownBy(() -> Deck.restore(null, "Sample Deck", "A test deck", ownerId, flashcardIds))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Deck ID is required");
    }

    @Test
    void throwExceptionWhenOwnerIdIsNull() {
        // --- Arrange & Act & Assert
        assertThatThrownBy(() -> Deck.createNew("Sample Deck", "A test deck", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Owner ID is required");
    }

    @Test
    void restoredDeckEqualsOriginalDeck() {
        // --- Arrange & Act
        Deck originalDeck = Deck.createNew("Sample Deck", "A test deck", UserId.generate());
        Deck restoredDeck = Deck.restore(originalDeck.deckId(), originalDeck.name(), originalDeck.description(), originalDeck.ownerId(), originalDeck.flashcardIds());

        // --- Assert
        assertThat(restoredDeck).isEqualTo(originalDeck);
    }

    @Test
    void addFlashcardToEmptyDeck() {
        // --- Arrange
        UserId ownerId = UserId.generate();
        FlashcardId flashcardId = FlashcardId.generate();
        Deck deck = Deck.createNew("Sample Deck", "A test deck", ownerId);

        // --- Act
        Deck updatedDeck = deck.addFlashcard(flashcardId);

        // --- Assert
        assertThat(updatedDeck.flashcardIds()).contains(flashcardId);
        assertThat(updatedDeck.flashcardIds()).hasSize(1);
    }

    @Test
    void throwExceptionWhenFlashcardAlreadyExists() {
        // --- Arrange
        UserId ownerId = UserId.from(UUID.randomUUID().toString());
        FlashcardId flashcardId = FlashcardId.generate();
        Deck deck = Deck.createNew("Sample Deck", "A test deck", ownerId).addFlashcard(flashcardId);

        // --- Act & Assert
        assertThatThrownBy(() -> deck.addFlashcard(flashcardId))
                .isInstanceOf(FlashcardAlreadyExistsException.class)
                .hasMessageContaining("Flashcard already exists in deck");
    }
}
