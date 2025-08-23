package com.clemnjord.flashcii.application.port.input.user;

import com.clemnjord.flashcii.domain.model.user.User;

public interface CreateUserUseCase {
    User execute(CreateUserCommand command);
}
