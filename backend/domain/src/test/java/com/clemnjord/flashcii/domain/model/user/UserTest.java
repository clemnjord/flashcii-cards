package com.clemnjord.flashcii.domain.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

  @Test
  void shouldCreateUserSuccessfullyWhenInputIsValid() {
    User user = User.createNew(new Username("testuser"));

    assertThat(user).isNotNull();
    assertThat(user.userId()).isNotNull();
    assertThat(user.username().value()).hasToString("testuser");
  }

  @Test
  void shouldThrowExceptionWhenUserIdIsNull() {
    UserId userId = UserId.generate();
    Assertions.assertThatThrownBy(() -> new User(userId, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Username cannot be null");
  }

  @Test
  void restoredUserShouldEqualOriginalUser() {
    // --- Arrange & Act
    User originalUser = User.createNew(new Username("testuser"));
    User restoredUser = User.restore(originalUser.userId(), originalUser.username());

    // --- Assert
    assertThat(restoredUser).isEqualTo(originalUser);
  }
}
