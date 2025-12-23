package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.persistence.entity.FlashcardEntityId;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.Rating;
import io.github.openspacedrepetition.Scheduler;
import io.github.openspacedrepetition.State;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class JpaFlashcardStatisticRepositoryTest {
    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    private JpaFlashcardRepository flashcardRepository;

    @Autowired
    private JpaFlashcardStatisticRepository flashcardStatisticRepository;

    User user;
    Flashcard flashcard;
    Card statisticCard;

    @BeforeEach
    void setUp(@Autowired EntityManager entityManager) {
        user = User.createNew(new Username("testUser"));
        userRepository.save(user);

        flashcard = Flashcard.createNew(new Question("A question."), new Answer("An answer."));
        flashcardRepository.save(flashcard, user.userId());

        Scheduler scheduler = Scheduler.builder().build();
        statisticCard =
                scheduler.reviewCard(Card.builder().build(), Rating.GOOD).card();
        statisticCard.setState(State.RELEARNING);
        statisticCard.setDue(Instant.now().truncatedTo(java.time.temporal.ChronoUnit.MICROS));
        statisticCard.setLastReview(Instant.now().minusSeconds(10).truncatedTo(ChronoUnit.MICROS));
        flashcardStatisticRepository.save(flashcard.flashcardId(), user.userId(), statisticCard);
        entityManager.flush();
    }

    @Test
    @DisplayName("Save persists flashcard statistic to the database")
    void save(@Autowired DataSource dataSource) {
        // --- Given & When
        var dsWrapper = new TransactionAwareDataSourceProxy(dataSource);
        var assertDbConnection = AssertDbConnectionFactory.of(dsWrapper).create();
        var flashcardEntityId = new FlashcardEntityId(
                flashcard.flashcardId().uuid(), user.userId().uuid());

        // Then
        Table flashcardStatTable =
                assertDbConnection.table("flashcard_statistics").build();

        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("flashcard_id")
                .value()
                .isEqualTo(flashcardEntityId.getFlashcardId());
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("owner_id")
                .value()
                .isEqualTo(flashcardEntityId.getOwnerId());
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("state")
                .value()
                .isEqualTo(statisticCard.getState().ordinal());
        Assertions.assertThat(flashcardStatTable).row().column("step").value().isEqualTo(statisticCard.getStep());
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("stability")
                .value()
                .isEqualTo(statisticCard.getStability());
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("difficulty")
                .value()
                .isEqualTo(statisticCard.getDifficulty());
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("due")
                .value()
                .isEqualTo(Timestamp.from(statisticCard.getDue()));
        Assertions.assertThat(flashcardStatTable)
                .row()
                .column("last_review")
                .value()
                .isEqualTo(Timestamp.from(statisticCard.getLastReview()));
    }

    @Test
    @DisplayName("Get returns statistic for given flashcard and user")
    void getPresent() {
        // Given & When
        Optional<Card> retrievedCard = flashcardStatisticRepository.get(flashcard.flashcardId(), user.userId());

        // Then
        assertThat(retrievedCard).isPresent().get().satisfies(card -> {
            assertThat(card.getState()).isEqualTo(statisticCard.getState());
            assertThat(card.getStep()).isEqualTo(statisticCard.getStep());
            assertThat(card.getStability()).isEqualTo(statisticCard.getStability());
            assertThat(card.getDifficulty()).isEqualTo(statisticCard.getDifficulty());
            assertThat(card.getDue()).isEqualTo(statisticCard.getDue());
            assertThat(card.getLastReview()).isEqualTo(statisticCard.getLastReview());
        });
    }

    @Test
    @DisplayName("Get returns empty when no statistic is found for given flashcard and user")
    void getEmpty() {
        // Given & When
        Optional<Card> retrievedCard = flashcardStatisticRepository.get(FlashcardId.generate(), user.userId());

        // Then
        assertThat(retrievedCard).isEmpty();
    }
}
