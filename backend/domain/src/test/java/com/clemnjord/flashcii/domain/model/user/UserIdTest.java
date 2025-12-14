package com.clemnjord.flashcii.domain.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    void shouldThrow_whenUUIDIsNull() {
        assertThatThrownBy(() -> new UserId(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void generateNewUserId() {
        // --- Arrange & Act
        UserId userId = UserId.generate();

        // --- Assert
        assertThat(userId).isNotNull();
    }

    @Test
    void createUserIdWhenFromStringUuidIsValid() {
        // --- Arrange & Act
        UserId userId = UserId.from("12345678-1234-1234-1234-123456789abc");

        // --- Assert
        assertThat(userId).isNotNull();
        assertThat(userId.uuid()).hasToString("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    void throwExceptionWhenFromStringUuidIsInvalid() {
        // --- Arrange & Act & Assert
        assertThatThrownBy(() -> UserId.from("invalid user id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user ID format: invalid user id");
    }
}
