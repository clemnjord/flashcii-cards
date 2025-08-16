package com.clemnjord.flashcii.application.usecase.user;

import com.clemnjord.flashcii.application.port.input.user.CreateUserCommand;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    void shouldCreateUserWhenUsernameIsUnique() {
        // Arrange
        mockUserDoesNotExist();

        var createUserCommand = new CreateUserCommand("testuser");

        // Act
        var result = createUserUseCase.execute(createUserCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(new Username("testuser"));
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
    private void mockUserExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true);
    }

    private void mockUserDoesNotExist() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
    }
}
