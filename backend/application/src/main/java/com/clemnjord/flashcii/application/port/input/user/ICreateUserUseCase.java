package com.clemnjord.flashcii.application.port.input.user;

import com.clemnjord.flashcii.domain.model.user.User;

import java.util.Objects;

public interface ICreateUserUseCase {
  record CreateUserCommand(String username) {
    public CreateUserCommand {
      Objects.requireNonNull(username, "Username cannot be null");
    }
  }

  User execute(CreateUserCommand command);
}
