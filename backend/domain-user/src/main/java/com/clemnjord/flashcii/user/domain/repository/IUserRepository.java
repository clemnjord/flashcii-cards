package com.clemnjord.flashcii.user.domain.repository;

import com.clemnjord.flashcii.shared.kernel.UserId;
import com.clemnjord.flashcii.user.domain.model.User;

public interface IUserRepository {
    User save(User user);

    boolean existsByUsername(String username);

    User findById(UserId user);
}
