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
        testUser.setUuid(UUID.randomUUID());
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
        FlashcardEntityId flashcardId = new FlashcardEntityId(UUID.randomUUID(), testUser.getUuid());
        testFlashcard = new FlashcardEntity();
        testFlashcard.setID(flashcardId);
        testFlashcard.setQuestion("What is JPA?");
        testFlashcard.setAnswer("Java Persistence API");
        flashcardRepository.save(testFlashcard);
    }

    @Test
    void testCreateDeckFlashcardEntity_shouldPersistSuccessfully() {
        // Given
        DeckFlashcardEntity deckFlashcard = new DeckFlashcardEntity(testDeck, testFlashcard);

        // When
        DeckFlashcardEntity savedEntity = deckFlashcardRepository.save(deckFlashcard);
        deckFlashcardRepository.flush();

        // Then
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getId().getDeckId()).isEqualTo(testDeck.getUUID());
        assertThat(savedEntity.getId().getFlashcardId())
                .isEqualTo(testFlashcard.getID().getFlashcardId());
        assertThat(savedEntity.getId().getOwnerId())
                .isEqualTo(testFlashcard.getID().getOwnerId());
    }

    @Test
    void testCreateDeckFlashcardEntity_shouldSetForeignKeysCorrectly() {
        // Given
        DeckFlashcardEntity deckFlashcard = new DeckFlashcardEntity(testDeck, testFlashcard);

        // When
        deckFlashcardRepository.saveAndFlush(deckFlashcard);

        // Clear the persistence context to force a fresh load from DB
        deckFlashcardRepository.flush();

        DeckFlashcardEntityId id = new DeckFlashcardEntityId(
                testDeck.getUUID(),
                testFlashcard.getID().getFlashcardId(),
                testFlashcard.getID().getOwnerId());

        DeckFlashcardEntity retrievedEntity =
                deckFlashcardRepository.findById(id).orElseThrow();

        // Then
        assertThat(retrievedEntity.getDeck()).isNotNull();
        assertThat(retrievedEntity.getDeck().getUUID()).isEqualTo(testDeck.getUUID());
        assertThat(retrievedEntity.getFlashcard()).isNotNull();
        assertThat(retrievedEntity.getFlashcard().getID().getFlashcardId())
                .isEqualTo(testFlashcard.getID().getFlashcardId());
    }

    @Test
    void testDeckEntity_addFlashcard_shouldCreateDeckFlashcardEntity() {
        // Given - deck and flashcard already created in setUp

        // When
        testDeck.addFlashcard(testFlashcard);
        deckRepository.saveAndFlush(testDeck);

        // Then
        DeckEntity retrievedDeck = deckRepository.findById(testDeck.getUUID()).orElseThrow();
        assertThat(retrievedDeck.getFlashcards()).hasSize(1);
        assertThat(retrievedDeck.getFlashcards().getFirst().getID().getFlashcardId())
                .isEqualTo(testFlashcard.getID().getFlashcardId());
    }
}
