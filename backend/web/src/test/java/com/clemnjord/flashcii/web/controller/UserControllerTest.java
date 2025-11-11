package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.clemnjord.flashcii.web.dto.FlashcardDto.FlashcardCreateRequest;
import static com.clemnjord.flashcii.web.dto.FlashcardDto.FlashcardResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateFlashcardUseCase createFlashcardUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldCreateFlashcard() {
        // --- Given
        Flashcard flashcard = Flashcard.restore(FlashcardId.generate(), new Question("Question"), new Answer("Answer"));
        when(createFlashcardUseCase.execute(any())).thenReturn(flashcard);

        // --- When
        FlashcardCreateRequest request = new FlashcardCreateRequest("Question", "Answer");

        FlashcardResponse response =
                userController.createFlashcard(UUID.randomUUID().toString(), request);

        // --- Then
        assertThat(response.uuid()).isEqualTo(flashcard.flashcardId().uuid().toString());
        assertThat(response.question()).isEqualTo(flashcard.question().value());
        assertThat(response.answer()).isEqualTo(flashcard.answer().value());
    }
}
