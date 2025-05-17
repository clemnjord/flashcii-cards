package com.clemnjord.flashcii.user.domain.usecase;

import com.clemnjord.flashcii.user.domain.command.CreateUserCommand;
import com.clemnjord.flashcii.user.domain.exception.UserAlreadyExists;
import com.clemnjord.flashcii.user.domain.model.User;
import com.clemnjord.flashcii.user.domain.repository.IUserRepository;

public class CreateUserUseCase {

    private final IUserRepository userRepository;

    public CreateUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserCommand command) {
        // Check if the user already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExists("User already exists with username: " + command.username());
        }

        // Create a new user
        User user = User.builder().username(command.username()).build();

        // Save the user to the repository
        return userRepository.save(user);
    }
}
