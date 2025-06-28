package com.clemnjord.flashcii.service.user.mapper;

import com.clemnjord.flashcii.application.user.command.CreateUserCommand;
import com.clemnjord.flashcii.domain.user.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserCommandMapper {
  User fromCreateUserCommand(CreateUserCommand command);
}
