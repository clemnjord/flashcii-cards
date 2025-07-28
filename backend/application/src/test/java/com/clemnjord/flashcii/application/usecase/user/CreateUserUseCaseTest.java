package com.clemnjord.flashcii.application.usecase.user;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.output.IUserRepository;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.User;
import com.clemnjord.flashcii.domain.model.UserId;
import com.clemnjord.flashcii.domain.model.Username;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    mockRepositorySave();
    mockUserDoesNotExist();

    var createUserCommand = new CreateUserUseCase.CreateUserCommand("testuser");

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

    var createUserCommand = new CreateUserUseCase.CreateUserCommand("testuser");

    // Act & Assert
    assertThatThrownBy(() -> createUserUseCase.execute(createUserCommand))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessageContaining("User already exists with username: testuser");
  }

  // --- Helpers ---
  private void mockRepositorySave() {
    when(userRepository.save(any()))
        .thenAnswer(
            invocation -> {
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
