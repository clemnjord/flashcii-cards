package com.clemnjord.flashcii.application.port.input;

import com.clemnjord.flashcii.domain.model.User;

public interface ICreateUserUseCase {
    record CreateUserCommand(String username) {}
    User execute(CreateUserCommand command);
}
