package com.clemnjord.flashcii.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaDeckDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaDeckFlashcardDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaFlashcardDao;
import com.clemnjord.flashcii.infrastructure.persistence.repository.JpaUserDao;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfiguration.class)
@Transactional
class DeckFlashcardEntityTest {

    @Autowired
    private JpaUserDao userRepository;

    @Autowired
    private JpaDeckDao deckRepository;

    @Autowired
    private JpaFlashcardDao flashcardRepository;

    @Autowired
    private JpaDeckFlashcardDao deckFlashcardRepository;

    private DeckEntity testDeck;
    private FlashcardEntity testFlashcard;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        UserEntity testUser = new UserEntity();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        userRepository.save(testUser);

        // Create and save a test deck
        testDeck = new DeckEntity();
        testDeck.setId(UUID.randomUUID());
        testDeck.setName("Test Deck");
        testDeck.setDescription("A test deck");
        testDeck.setOwner(testUser);
        deckRepository.save(testDeck);

        // Create and save a test flashcard
        FlashcardEntityId flashcardId = new FlashcardEntityId(UUID.randomUUID(), testUser.getId());
        testFlashcard = new FlashcardEntity();
        testFlashcard.setId(flashcardId);
        testFlashcard.setQuestion("What is JPA?");
        testFlashcard.setAnswer("Java Persistence API");
        flashcardRepository.save(testFlashcard);
    }

    @Test
    void testCreateDeckFlashcardEntity_shouldPersistSuccessfully() {
        // Given
        DeckFlashcardEntity deckFlashcard = new DeckFlashcardEntity(testDeck, testFlashcard);

        // When
        DeckFlashcardEntity savedEntity = deckFlashcardRepository.saveAndFlush(deckFlashcard);

        // Then
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId().getDeckId()).isEqualTo(testDeck.getId());
        assertThat(savedEntity.getId().getFlashcardId())
                .isEqualTo(testFlashcard.getId().getFlashcardId());
        assertThat(savedEntity.getId().getOwnerId())
                .isEqualTo(testFlashcard.getId().getOwnerId());
    }

    @Test
    void testCreateDeckFlashcardEntity_shouldSetForeignKeysCorrectly() {
        // Given
        DeckFlashcardEntity deckFlashcard = new DeckFlashcardEntity(testDeck, testFlashcard);

        // When
        deckFlashcardRepository.saveAndFlush(deckFlashcard);

        DeckFlashcardEntityId id = new DeckFlashcardEntityId(
                testDeck.getId(),
                testFlashcard.getId().getFlashcardId(),
                testFlashcard.getId().getOwnerId());

        DeckFlashcardEntity retrievedEntity =
                deckFlashcardRepository.findById(id).orElseThrow();

        // Then
        assertThat(retrievedEntity.getDeck()).isNotNull();
        assertThat(retrievedEntity.getDeck().getId()).isEqualTo(testDeck.getId());
        assertThat(retrievedEntity.getFlashcard()).isNotNull();
        assertThat(retrievedEntity.getFlashcard().getId().getFlashcardId())
                .isEqualTo(testFlashcard.getId().getFlashcardId());
    }
}
