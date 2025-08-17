package com.clemnjord.flashcii.application.usecase.user;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.user.CreateUserCommand;
import com.clemnjord.flashcii.application.port.input.user.CreateUserUseCase;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationService
@ApplicationTransactional
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserUseCaseImpl.class);


    private final IUserRepository userRepository;

    public CreateUserUseCaseImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(CreateUserCommand command) {
        logger.debug("Creating user with username '{}'", command.username());

        // Check if the user already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new UserAlreadyExistsException(
                    "User already exists with username: " + command.username());
        }

        // Map the command to a User entity
        User user = User.createNew(new Username(command.username()));

        // Save the user to the repository
        userRepository.save(user);

        logger.debug("Successfully created deck with ID: {}", user.userId().uuid());
        return user;
    }
}
