package com.clemnjord.flashcii.application.user.usecase;

import com.clemnjord.flashcii.application.user.command.CreateUserCommand;
import com.clemnjord.flashcii.domain.user.model.User;

public interface ICreateUserUseCase {
  User execute(CreateUserCommand command);
}
