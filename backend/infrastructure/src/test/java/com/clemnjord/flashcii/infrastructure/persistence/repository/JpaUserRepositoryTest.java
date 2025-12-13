package com.clemnjord.flashcii.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import com.clemnjord.flashcii.infrastructure.testcontainers.PostgresTestContainerExtension;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.AssertDbConnectionFactory;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(PostgresTestContainerExtension.class)
@ActiveProfiles("test")
@Transactional
class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository userRepository;

    private final User user = new User(UserId.generate(), new Username("testuser"));

    @Nested
    @DisplayName("Save operations")
    class SaveOperations {

        @Test
        @DisplayName("Should persist user")
        void shouldSaveUser(@Autowired EntityManager entityManager, @Autowired DataSource dataSource) {
            // When
            userRepository.save(user);
            entityManager.flush();

            // Then
            var dsWrapper = new TransactionAwareDataSourceProxy(dataSource);
            var assertDbConnection = AssertDbConnectionFactory.of(dsWrapper).create();
            Table usersTable = assertDbConnection.table("users").build();

            Assertions.assertThat(usersTable)
                    .row()
                    .column("id")
                    .value()
                    .isEqualTo(user.userId().uuid());
            Assertions.assertThat(usersTable).row().column("username").value().isEqualTo("testuser");
        }
    }

    @Nested
    @DisplayName("Find operations")
    class FindOperations {

        @Test
        @DisplayName("Should find user by username")
        void shouldFindUserByUsername() {
            // Given
            userRepository.save(user);

            // When
            Optional<User> foundUser = userRepository.findByUsername("testuser");

            // Then
            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().userId()).isEqualTo(user.userId());
            assertThat(foundUser.get().username().value()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            // When
            Optional<User> foundUser = userRepository.findByUsername("nonexistent");

            // Then
            assertThat(foundUser).isEmpty();
        }
    }
}
