package com.clemnjord.flashcii.domain.user.repository;


import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.model.UserId;

import java.util.Optional;

public interface IUserRepository {
    User save(User user);

    boolean existsByUsername(String username);

    Optional<User> findById(UserId user);
}
