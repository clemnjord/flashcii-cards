package com.clemnjord.flashcii.spring.shared.usecase;

import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Stub component until there is a real authentication process with an identity provider.
 * Until then, use this to ger a user and create it if it doesn't exist.
 */
@Component
public class StubCurrentUserUseCase implements ICurrentUserUseCase {

    IUserRepository userRepository;

    public StubCurrentUserUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        UserId currentUserId = new UserId(UUID.fromString("8672ef18-63b7-46c4-bf58-e7c2ca0e7730"));

        Optional<User> currentUser = userRepository.findById(currentUserId);

        if (currentUser.isPresent()) {
            return currentUser.get();
        } else {
            User newUser = new User(currentUserId, new Username("test"));
            return userRepository.save(newUser);
        }
    }
}
