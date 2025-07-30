package com.clemnjord.flashcii.cli.usecase;

import com.clemnjord.flashcii.application.port.output.ICurrentUserUseCase;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserUseCase implements ICurrentUserUseCase {
    @Override
    public User getCurrentUser() {
        System.out.println("Getting current user from CLI!");
        return new User(
                new UserId(UUID.fromString("8672ef18-63b7-46c4-bf58-e7c2ca0e7730")), new Username("test"));
    }
}
