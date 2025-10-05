package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.output.IAuthorizationService;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.exception.user.UnauthorizedException;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.User;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.domain.model.user.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFlashcardUseCaseImplTest {

    private static final String DEFAULT_QUESTION = "What is Flashcii?";
    private static final String DEFAULT_ANSWER = "A flashcard app";
    private final User testUser = User.createNew(new Username("testUser"));

    @Mock
    private IFlashcardRepository flashcardRepository;

    @Mock
    private IUserContextService userContextService;

    @Mock
    private IAuthorizationService authorizationService;

    @InjectMocks
    private CreateFlashcardUseCaseImpl createFlashcardUseCaseImpl;

    @BeforeEach
    void setUp() {
        when(userContextService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void shouldCreateFlashcard() {
        // Arrange
        var createCardCommand = createFlashcardCommand();
        when(authorizationService.canManageResourceFor(any(), any())).thenReturn(true);

        // Act
        var result = createFlashcardUseCaseImpl.execute(createCardCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.question().value()).isEqualTo(DEFAULT_QUESTION);
        assertThat(result.answer().value()).isEqualTo(DEFAULT_ANSWER);

        verify(userContextService).getCurrentUser();
    }

    @Test
    void shouldThrowWhenUnauthorized() {
        // Arrange
        var createCardCommand = createFlashcardCommand();
        when(authorizationService.canManageResourceFor(any(), any())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> createFlashcardUseCaseImpl.execute(createCardCommand))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining(
                        "User " + testUser.userId().uuid() + " is not authorized to create flashcards for user "
                                + createCardCommand.ownerId().uuid());
    }

    // Helper methods for test data creation
    private CreateFlashcardCommand createFlashcardCommand(UserId ownerId) {
        return new CreateFlashcardCommand(ownerId, new Question(DEFAULT_QUESTION), new Answer(DEFAULT_ANSWER));
    }

    private CreateFlashcardCommand createFlashcardCommand() {
        return createFlashcardCommand(testUser.userId());
    }
}
