package com.clemnjord.flashcii.service.user;

import com.clemnjord.flashcii.domain.user.exception.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.user.model.User;
import com.clemnjord.flashcii.domain.user.model.UserId;
import com.clemnjord.flashcii.domain.user.repository.IUserRepository;
import com.clemnjord.flashcii.service.user.command.CreateUserCommand;
import com.clemnjord.flashcii.service.user.usecase.CreateUserUseCase;
import com.clemnjord.flashcii.service.user.mapper.UserCommandMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CreateUserUseCaseTest {

    IUserRepository userRepository;
    CreateUserUseCase createUserUseCase;
    UserCommandMapper userCommandMapper;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userCommandMapper = Mappers.getMapper(UserCommandMapper.class);
        createUserUseCase = new CreateUserUseCase(userRepository, userCommandMapper);
    }

    @Test
    void shouldCreateUserWhenUsernameIsUnique() {
        // Arrange
        mockRepositorySave();
        mockUserDoesNotExist();

        var createUserCommand = new CreateUserCommand("testuser");

        // Act
        var result = createUserUseCase.execute(createUserCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("testuser");
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        // Arrange
        mockUserExists();

        var createUserCommand = new CreateUserCommand("testuser");

        // Act & Assert
        assertThatThrownBy(() -> createUserUseCase.execute(createUserCommand))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists with username: testuser");
    }

    // --- Helpers ---
    private void mockRepositorySave() {
        when(userRepository.save(any())).thenAnswer(invocation -> {
            var user = (User) invocation.getArgument(0);

            var userId = new UserId(UUID.randomUUID());
            return new User(userId, user.username());
        });
    }

    private void mockUserExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true);
    }

    private void mockUserDoesNotExist() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
    }
}
