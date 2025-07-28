package com.clemnjord.flashcii.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void testNominalUser() {
    UserId userId = new UserId(UUID.randomUUID());
    User user = new User(userId, new Username("testuser"));

    assertThat(user.username()).isEqualTo(new Username("testuser"));
    assertThat(user.userId()).isEqualTo(userId);
  }

  @Test
  void testUserWithoutName() {
    UserId userId = new UserId(UUID.randomUUID());
    Assertions.assertThatThrownBy(() -> new User(userId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Username cannot be null");
  }
}
