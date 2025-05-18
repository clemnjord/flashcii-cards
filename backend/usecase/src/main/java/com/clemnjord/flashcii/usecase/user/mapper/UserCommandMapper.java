package com.clemnjord.flashcii.usecase.user.mapper;

import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.usecase.user.command.CreateUserCommand;
import org.mapstruct.Mapper;

@Mapper
public interface UserCommandMapper {
    User fromCreateUserCommand(CreateUserCommand command);
}

