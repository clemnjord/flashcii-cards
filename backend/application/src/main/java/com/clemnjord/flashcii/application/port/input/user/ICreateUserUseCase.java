package com.clemnjord.flashcii.application.port.input.user;

import com.clemnjord.flashcii.domain.model.user.User;


public interface ICreateUserUseCase {
    User execute(CreateUserCommand command);
}
