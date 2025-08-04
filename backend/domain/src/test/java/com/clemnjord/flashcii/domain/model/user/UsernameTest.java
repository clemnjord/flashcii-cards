package com.clemnjord.flashcii.domain.model.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UsernameTest {
  @Test
  void createNewUsernameWhenInputIsValid() {
    Username username = new Username("testuser");
    Assertions.assertThat(username.value()).isEqualTo("testuser");
  }

  @Test
  void throwExceptionWhenUsernameIsNull() {
    Assertions.assertThatThrownBy(() -> new Username(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Username cannot be null");
  }

  @Test
  void throwExceptionWhenUsernameIsTooShort() {
    Assertions.assertThatThrownBy(() -> new Username("aa"))
        .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username too short (min 3 characters)");
  }

  @Test
  void throwExceptionWhenUsernameIsTooLong() {
    String longUsername = "a".repeat(256);
    Assertions.assertThatThrownBy(() -> new Username(longUsername))
        .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Username too long (max 30 characters)");
  }

  @Test
  void throwExceptionWhenUsernameIsInvalid() {
    Assertions.assertThatThrownBy(() -> new Username("invalid name!"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            "Username can only contain letters, numbers, underscores and hyphens");
  }
}
