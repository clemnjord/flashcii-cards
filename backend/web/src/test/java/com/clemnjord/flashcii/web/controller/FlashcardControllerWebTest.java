package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.web.dto.FlashcardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlashcardController.class)
class FlashcardControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateFlashcardUseCase createFlashcardUseCase;

    @Test
    void shouldCreateFlashcard() throws Exception {
        // --- Given
        var request = new FlashcardDto.FlashcardCreateRequest(UUID.randomUUID().toString(), "Question", "Answer");
        Flashcard mockFlashcard = new Flashcard(FlashcardId.generate(), new Question("Question"), new Answer("Answer"));

        when(createFlashcardUseCase.execute(any())).thenReturn(mockFlashcard);

        // --- When & Then
        mockMvc.perform(post("/flashcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid")
                        .value(mockFlashcard.flashcardId().uuid().toString()))
                .andExpect(jsonPath("$.question").value("Question"))
                .andExpect(jsonPath("$.answer").value("Answer"));
    }
}
