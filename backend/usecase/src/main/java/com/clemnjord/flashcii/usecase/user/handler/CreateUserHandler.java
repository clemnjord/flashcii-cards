package com.clemnjord.flashcii.usecase.user.handler;


import com.clemnjord.flashcii.domain.user.exception.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.repository.IUserRepository;
import com.clemnjord.flashcii.usecase.user.command.CreateUserCommand;

public class CreateUserHandler {

    private final IUserRepository userRepository;

    public CreateUserHandler(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserCommand command) {
        // Check if the user already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExistsException("User already exists with username: " + command.username());
        }

        // Create a new user
        User user = User.builder().username(command.username()).build();

        // Save the user to the repository
        return userRepository.save(user);
    }
}
