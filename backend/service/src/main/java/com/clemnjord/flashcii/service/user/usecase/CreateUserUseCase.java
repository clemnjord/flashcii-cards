package com.clemnjord.flashcii.service.user.usecase;

import com.clemnjord.flashcii.application.user.command.CreateUserCommand;
import com.clemnjord.flashcii.application.user.usecase.ICreateUserUseCase;
import com.clemnjord.flashcii.domain.user.exception.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.repository.IUserRepository;
import com.clemnjord.flashcii.service.user.mapper.UserCommandMapper;

public class CreateUserUseCase implements ICreateUserUseCase {

  private final IUserRepository userRepository;
  private final UserCommandMapper userCommandMapper;

  public CreateUserUseCase(IUserRepository userRepository, UserCommandMapper userCommandMapper) {
    this.userRepository = userRepository;
    this.userCommandMapper = userCommandMapper;
  }

  public User execute(CreateUserCommand command) {
    // Check if the user already exists
    if (userRepository.existsByUsername(command.username())) {
      throw new UserAlreadyExistsException(
          "User already exists with username: " + command.username());
    }

    // Map the command to a User entity
    User user = userCommandMapper.fromCreateUserCommand(command);

    // Save the user to the repository
    return userRepository.save(user);
  }
}
