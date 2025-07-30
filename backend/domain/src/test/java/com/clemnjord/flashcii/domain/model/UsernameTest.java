package com.clemnjord.flashcii.domain.model;

import com.clemnjord.flashcii.domain.model.user.Username;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UsernameTest {
  @Test
  void shouldThrowIllegalArgumentExceptionWhenUsernameIsNull() {
    Assertions.assertThatThrownBy(() -> new Username(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Username cannot be null");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenUsernameIsBlank() {
    Assertions.assertThatThrownBy(() -> new Username(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Username cannot be blank");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenUsernameIsTooLong() {
    String longUsername = "a".repeat(256);
    Assertions.assertThatThrownBy(() -> new Username(longUsername))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Username must be between 3 and 50 characters");
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenUsernameIsInvalid() {
    Assertions.assertThatThrownBy(() -> new Username("invalid name!"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            "Username can only contain letters, numbers, underscores and hyphens");
  }
}
