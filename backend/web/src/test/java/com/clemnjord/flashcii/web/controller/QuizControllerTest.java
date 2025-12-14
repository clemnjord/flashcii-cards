package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.deck.AddCardToDeckUseCase;
import com.clemnjord.flashcii.application.port.input.deck.CreateDeckUseCase;
import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizCommand;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizUseCase;
import com.clemnjord.flashcii.application.usecase.flashcard.RateFlashcardUseCaseImpl;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.web.dto.QuizDto;
import io.github.openspacedrepetition.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateFixedSizedQuizUseCase createQuizUseCase;

    @MockitoBean
    private RateFlashcardUseCaseImpl rateFlashcardUseCase;

    QuizId quizId = QuizId.generate();
    DeckId deckId = DeckId.generate();
    FlashcardId flashcardId1 = FlashcardId.generate();
    FlashcardId flashcardId2 = FlashcardId.generate();

    @Test
    void shouldCreateQuiz() {
        // --- Given
        var quiz = new FixedSizedQuiz(quizId, List.of(flashcardId1, flashcardId2));
        var expectedCommand = new CreateFixedSizedQuizCommand(10, Set.of(DeckId.from(deckId.uuid().toString())));
        when(createQuizUseCase.execute(expectedCommand)).thenReturn(quiz);

        QuizDto.CreateQuizRequest request = new QuizDto.CreateQuizRequest(List.of(deckId.uuid().toString()), 10);

        // --- When & Then
        mockMvcTester.post()
                .uri("/quiz")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
                .assertThat()
                .hasStatus(201).bodyJson().convertTo(QuizDto.QuizResponse.class)
                .satisfies(r -> {
                    assertThat(r.uuid()).isEqualTo(quizId.uuid().toString());
                    assertThat(r.flashcardIds()).containsExactlyInAnyOrder(
                            flashcardId1.uuid().toString(), flashcardId2.uuid().toString());
                });
    }

    @Test
    void shouldRateFlashcard() {
        // --- Given
        var expectedCommand = new RateFlashcardCommand( quizId, flashcardId1, Rating.GOOD);
        QuizDto.RateFlashcardRequest request = new QuizDto.RateFlashcardRequest(quizId.uuid().toString(), flashcardId1.uuid().toString(), Rating.GOOD.name());

        doNothing().when(rateFlashcardUseCase).execute(expectedCommand);

        // --- When & Then
        mockMvcTester.post()
                .uri("/quiz/" + quizId.uuid().toString() + "/rate")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
                .assertThat()
                .hasStatus(204);
    }
}
