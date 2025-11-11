package com.clemnjord.flashcii.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaDeckDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaFlashcardDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaUserDao;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
        // Given
        // Create and save a test user
        UserEntity testUser = new UserEntity();
        testUser.setUuid(UUID.randomUUID());
        testUser.setUsername("testuser");
        userRepository.save(testUser);

        // Create and save a test deck
        DeckEntity testDeck = new DeckEntity();
        testDeck.setId(UUID.randomUUID());
        testDeck.setName("Test Deck");
        testDeck.setDescription("A test deck");
        testDeck.setOwner(testUser);
        deckRepository.save(testDeck);

        // Create and save a test flashcard
        FlashcardEntityId flashcardId = new FlashcardEntityId(UUID.randomUUID(), testUser.getUuid());
        FlashcardEntity testFlashcard = new FlashcardEntity();
        testFlashcard.setID(flashcardId);
        testFlashcard.setQuestion("What is JPA?");
        testFlashcard.setAnswer("Java Persistence API");
        flashcardRepository.save(testFlashcard);

        // When
        testDeck.addFlashcard(testFlashcard);
        deckRepository.saveAndFlush(testDeck);

        // Then
        DeckEntity retrievedDeck = deckRepository.findById(testDeck.getUUID()).orElseThrow();
        assertThat(retrievedDeck.getFlashcards()).hasSize(1);
        assertThat(retrievedDeck.getFlashcards().get(0).getID().getFlashcardId())
                .isEqualTo(testFlashcard.getID().getFlashcardId());
    }
}
