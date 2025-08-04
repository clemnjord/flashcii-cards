package com.clemnjord.flashcii.domain.model.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @Test
    void shouldGenerateNewUserIdSuccessfully() {
        // --- Arrange & Act
        UserId userId = UserId.generate();

        // --- Assert
        assertThat(userId).isNotNull();
    }

    @Test
    void shouldCreateUserSuccessfullyWhenFromStringIsValid() {
        // --- Arrange & Act
        UserId userId = UserId.from("12345678-1234-1234-1234-123456789abc");

        // --- Assert
        assertThat(userId).isNotNull();
        assertThat(userId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void shouldThrowExceptionWhenFromStringIsInvalid() {
        // --- Arrange & Act & Assert
        assertThatThrownBy(() -> UserId.from("invalid user id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user ID format: invalid user id");
    }
}
