package com.clemnjord.flashcii.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.clemnjord.flashcii.infrastructure.persistence.TestJpaConfiguration;
import jakarta.persistence.PersistenceException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = TestJpaConfiguration.class)
@ActiveProfiles("test")
class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPersistValidUserEntity() {
        // Given
        UserEntity user = new UserEntity();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setUsername("testuser");

        // When
        UserEntity savedUser = entityManager.persistAndFlush(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(userId);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldValidateConstraints() {
        // Given
        UserEntity user = new UserEntity();
        // Missing required fields

        // When & Then
        assertThatThrownBy(() -> entityManager.persistAndFlush(user)).isInstanceOf(PersistenceException.class);
    }

    @Test
    void shouldHandleUniqueConstraints() {
        // Given
        UserEntity user1 = new UserEntity();
        user1.setId(UUID.randomUUID());
        user1.setUsername("testuser");

        UserEntity user2 = new UserEntity();
        user2.setId(UUID.randomUUID());
        user2.setUsername("testuser"); // Same username

        // When & Then
        entityManager.persist(user1);
        entityManager.flush();

        assertThatThrownBy(() -> {
                    entityManager.persistAndFlush(user2);
                })
                .isInstanceOf(PersistenceException.class);
    }
}
