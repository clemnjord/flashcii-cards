package com.clemnjord.flashcii.domain.user.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TestUser {

    @Test
    void testNominalUser() {
        UserId userId = new UserId(UUID.randomUUID());
        User user = new User(userId, "testuser");

        assertThat(user.username()).isEqualTo("testuser");
        assertThat(user.userId()).isEqualTo(userId);
    }

    @Test
    void testUserWithoutName() {
        UserId userId = new UserId(UUID.randomUUID());
        assertThatThrownBy(() -> new User(userId, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username cannot be null or blank");
    }

    @Test
    void testUserWithBlankName() {
        UserId userId = new UserId(UUID.randomUUID());
        assertThatThrownBy(() -> new User(userId, " "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username cannot be null or blank");
    }

    @Test
    void testUserWithLongName() {
        UserId userId = new UserId(UUID.randomUUID());
        String longName = "a".repeat(256); // 256 characters long
        assertThatThrownBy(() -> new User(userId, longName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username cannot be longer than 255 characters");
    }

    @Test
    void testUserWithInvalidName() {
        UserId userId = new UserId(UUID.randomUUID());
        assertThatThrownBy(() -> new User(userId, "invalid name!"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username can only contain alphanumeric characters and underscores");
    }


}
