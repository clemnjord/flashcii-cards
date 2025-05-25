package com.clemnjord.flashcii.service.user.mapper;

import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.service.user.command.CreateUserCommand;
import org.mapstruct.Mapper;

@Mapper
public interface UserCommandMapper {
    User fromCreateUserCommand(CreateUserCommand command);
}

