package com.clemnjord.flashcii.user.domain.usecase;

import com.clemnjord.flashcii.shared.kernel.UserId;
import com.clemnjord.flashcii.user.domain.command.CreateUserCommand;
import com.clemnjord.flashcii.user.domain.exception.UserAlreadyExists;
import com.clemnjord.flashcii.user.domain.model.User;
import com.clemnjord.flashcii.user.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CreateUserUseCaseTest {

    IUserRepository userRepository;
    CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        createUserUseCase = new CreateUserUseCase(userRepository);
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
        var result = createUserUseCase.execute(createUserCommand);

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
        assertThatThrownBy(() -> createUserUseCase.execute(createUserCommand))
                .isInstanceOf(UserAlreadyExists.class)
                .hasMessageContaining("User already exists with username: testuser");
    }
}
