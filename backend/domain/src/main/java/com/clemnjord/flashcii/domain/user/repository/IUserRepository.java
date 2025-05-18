package com.clemnjord.flashcii.domain.user.repository;


import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.model.UserId;

public interface IUserRepository {
    User save(User user);

    boolean existsByUsername(String username);

    User findById(UserId user);
}
