package com.clemnjord.flashcii.application.usecase.user;

import com.clemnjord.flashcii.application.port.input.ICreateUserUseCase;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.User;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.model.Username;

public class CreateUserUseCase implements ICreateUserUseCase {

    private final IUserRepository userRepository;

    public CreateUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(CreateUserCommand command) {
        // Check if the user already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExistsException(
                    "User already exists with username: " + command.username());
        }

        // Map the command to a User entity
        User user = User.builder().username(new Username(command.username())).build();

        // Save the user to the repository
        return userRepository.save(user);
    }
}
