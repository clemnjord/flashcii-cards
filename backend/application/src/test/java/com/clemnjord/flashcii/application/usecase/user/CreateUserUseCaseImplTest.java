package com.clemnjord.flashcii.application.usecase.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.user.CreateUserCommand;
import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {
    @Mock
    IUserRepository userRepository;

    @InjectMocks
    CreateUserUseCaseImpl createUserUseCaseImpl;

    @Test
    void shouldCreateUserWhenUsernameIsUnique() {
        // Arrange
        mockUserDoesNotExist();

        var createUserCommand = new CreateUserCommand("testuser");

        // Act
        var result = createUserUseCaseImpl.execute(createUserCommand);

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
        assertThatThrownBy(() -> createUserUseCaseImpl.execute(createUserCommand))
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
