package com.clemnjord.flashcii.usecase.user;

import com.clemnjord.flashcii.domain.user.exception.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.model.UserId;
import com.clemnjord.flashcii.domain.user.repository.IUserRepository;
import com.clemnjord.flashcii.usecase.user.command.CreateUserCommand;
import com.clemnjord.flashcii.usecase.user.handler.CreateUserHandler;
import com.clemnjord.flashcii.usecase.user.mapper.UserCommandMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CreateUserHandlerTest {

    IUserRepository userRepository;
    CreateUserHandler createUserHandler;
    UserCommandMapper userCommandMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userCommandMapper = Mappers.getMapper(UserCommandMapper.class);
        createUserHandler = new CreateUserHandler(userRepository, userCommandMapper);
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.save(any())).thenAnswer(invocation -> {
            var user = (User) invocation.getArgument(0);

            var userId = new UserId(UUID.randomUUID());
            return new User(userId, user.username());
        });

        when(userRepository.existsByUsername(any())).thenReturn(false);

        var createUserCommand = new CreateUserCommand("testuser");

        // Act
        var result = createUserHandler.execute(createUserCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
    }

    @Test
    void testCreateUserAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername(any())).thenReturn(true);

        var createUserCommand = new CreateUserCommand("testuser");

        // Act & Assert
        assertThatThrownBy(() -> createUserHandler.execute(createUserCommand))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists with username: testuser");
    }
}
