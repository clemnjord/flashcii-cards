package com.clemnjord.flashcii.domain.model;

import java.util.Objects;
import lombok.Builder;

@Builder
public record User(UserId userId, Username username) {
  public User {
    Objects.requireNonNull(username, "Username cannot be null");
  }
}
