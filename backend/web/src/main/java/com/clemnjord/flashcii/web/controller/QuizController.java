package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.RateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizCommand;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizUseCase;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.web.dto.QuizDto;
import io.github.openspacedrepetition.Rating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Quizzes", description = "Quiz management operations")
public class QuizController {
    private final CreateFixedSizedQuizUseCase createQuizUseCase;
    private final RateFlashcardUseCase rateFlashcardUseCase;

    @PostMapping
    @Operation(summary = "Create quiz", description = "Create a new quiz")
    @ApiResponse(responseCode = "201", description = "Quiz created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizDto.QuizResponse createQuiz(@Valid @RequestBody QuizDto.CreateQuizRequest request) {
        var command = new CreateFixedSizedQuizCommand(
                request.deckIds().stream().map(DeckId::from).collect(Collectors.toSet()));

        FixedSizedQuiz quiz = createQuizUseCase.execute(command);

        return new QuizDto.QuizResponse(
                quiz.getId().uuid().toString(),
                quiz.getFlashcardIds().stream().map(id -> id.uuid().toString()).toList());
    }

    @PostMapping("/{quizId}/rate")
    @Operation(summary = "Rate flashcard", description = "Rate a flashcard in a quiz")
    @ApiResponse(responseCode = "204", description = "Flashcard rated successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rateFlashcard(@PathVariable String quizId, @Valid @RequestBody QuizDto.RateFlashcardRequest request) {

        final Rating rating;
        try {
            rating = Rating.valueOf(request.rating());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating value: " + request.rating());
        }

        var command = new RateFlashcardCommand(QuizId.from(quizId), FlashcardId.from(request.flashcardUuid()), rating);

        rateFlashcardUseCase.execute(command);
    }
}
