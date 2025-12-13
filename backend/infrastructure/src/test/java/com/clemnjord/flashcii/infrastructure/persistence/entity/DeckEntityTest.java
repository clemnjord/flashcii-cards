package com.clemnjord.flashcii.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaDeckDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaFlashcardDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaUserDao;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfiguration.class)
@Transactional
class DeckEntityTest {
    @Autowired
    private JpaUserDao userRepository;

    @Autowired
    private JpaDeckDao deckRepository;

    @Autowired
    private JpaFlashcardDao flashcardRepository;

    @Test
    void testDeckEntity_addFlashcard_shouldCreateDeckFlashcardEntity() {
        // --- Given
        // Create and save a test user
        UserEntity testUser = new UserEntity(UUID.randomUUID(), "testuser", new ArrayList<>());
        userRepository.save(testUser);

        // Create and save a test deck
        DeckEntity testDeck =
                new DeckEntity(UUID.randomUUID(), "Test Deck", "A test deck", testUser, new ArrayList<>());
        deckRepository.save(testDeck);

        // Create and save a test flashcard
        FlashcardEntityId flashcardId = new FlashcardEntityId(UUID.randomUUID(), testUser.getId());
        FlashcardEntity testFlashcard =
                new FlashcardEntity(flashcardId, "What is JPA?", "Java Persistence API", new ArrayList<>());
        flashcardRepository.save(testFlashcard);

        // --- When
        testDeck.addFlashcard(testFlashcard);
        deckRepository.saveAndFlush(testDeck);

        // --- Then
        DeckEntity retrievedDeck = deckRepository.findById(testDeck.getId()).orElseThrow();
        assertThat(retrievedDeck.getFlashcards()).hasSize(1);
        assertThat(retrievedDeck.getFlashcards().getFirst().getId().getFlashcardId())
                .isEqualTo(testFlashcard.getId().getFlashcardId());
    }
}
