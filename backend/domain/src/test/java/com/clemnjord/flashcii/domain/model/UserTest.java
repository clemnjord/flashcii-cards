package com.clemnjord.flashcii.domain.model;

import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
