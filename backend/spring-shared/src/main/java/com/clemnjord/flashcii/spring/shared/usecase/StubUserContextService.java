package com.clemnjord.flashcii.spring.shared.usecase;

import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.Username;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Stub component until there is a real authentication process with an identity provider.
 * Until then, use this to ger a user and create it if it doesn't exist.
 */
@Component
public class StubUserContextService implements IUserContextService {

    IUserRepository userRepository;

    public StubUserContextService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        String username = "default";
        Optional<User> currentUser = userRepository.findByUsername(username);

        if (currentUser.isPresent()) {
            return currentUser.get();
        } else {
            User newUser = User.createNew(new Username(username));
            userRepository.save(newUser);
            return newUser;
        }
    }
}
