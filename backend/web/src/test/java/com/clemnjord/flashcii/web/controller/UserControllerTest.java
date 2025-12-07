package com.clemnjord.flashcii.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.web.dto.FlashcardDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvcTester mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateFlashcardUseCase createFlashcardUseCase;

    @Test
    void shouldCreateFlashcard() {
        // --- Given
        var request = new FlashcardDto.FlashcardCreateRequest("Question", "Answer");
        Flashcard mockFlashcard = new Flashcard(FlashcardId.generate(), new Question("Question"), new Answer("Answer"));

        when(createFlashcardUseCase.execute(any())).thenReturn(mockFlashcard);

        // --- When & Then
        mockMvc.post()
                .uri("/users/" + UUID.randomUUID() + "/flashcards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .assertThat()
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .convertTo(FlashcardDto.FlashcardResponse.class)
                .satisfies(response -> {
                    assertThat(response.question()).isEqualTo("Question");
                    assertThat(response.answer()).isEqualTo("Answer");
                });
    }
}
