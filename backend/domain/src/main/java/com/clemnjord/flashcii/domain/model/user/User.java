package com.clemnjord.flashcii.domain.model.user;

import lombok.Builder;

import java.util.Objects;

@Builder
public record User(UserId userId, Username username) {
  public User {
    Objects.requireNonNull(username, "Username cannot be null");
  }
}
