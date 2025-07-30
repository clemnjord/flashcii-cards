package com.clemnjord.flashcii.domain.model.user;

import java.util.Objects;

public record Username(String value) {
  public Username {
    Objects.requireNonNull(value, "Username cannot be null");
    if (value.isBlank()) {
      throw new IllegalArgumentException("Username cannot be blank");
    }
    if (value.length() < 3 || value.length() > 50) {
      throw new IllegalArgumentException("Username must be between 3 and 50 characters");
    }
    if (!value.matches("^[a-zA-Z0-9_-]+$")) {
      throw new IllegalArgumentException(
          "Username can only contain letters, numbers, underscores and hyphens");
    }
  }
}
