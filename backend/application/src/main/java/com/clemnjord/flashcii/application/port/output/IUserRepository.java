package com.clemnjord.flashcii.application.port.output;

import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;

import java.util.Optional;

public interface IUserRepository {
  User save(User user);

  boolean existsByUsername(String username);

  Optional<User> findById(UserId user);
}
