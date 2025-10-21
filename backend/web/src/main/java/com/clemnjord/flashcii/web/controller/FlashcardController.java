package com.clemnjord.flashcii.web.controller;

import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardCommand;
import com.clemnjord.flashcii.application.port.input.flashcard.CreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;
import com.clemnjord.flashcii.domain.model.user.UserId;
import com.clemnjord.flashcii.web.dto.FlashcardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/flashcards", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlashcardController {

    private final CreateFlashcardUseCase createFlashcardUseCase;

    public FlashcardController(CreateFlashcardUseCase createFlashcardUseCase) {
        this.createFlashcardUseCase = createFlashcardUseCase;
    }

    @PostMapping
    @Operation(summary = "Create flashcard", description = "Create a new flashcard")
    @ApiResponse(responseCode = "201", description = "Flashcard created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public FlashcardDto.FlashcardResponse createDeck(@Valid @RequestBody FlashcardDto.FlashcardCreateRequest request) {
        Flashcard flashcard = createFlashcardUseCase.execute(new CreateFlashcardCommand(
                UserId.from(request.userId()), new Question(request.question()), new Answer(request.answer())));

        return new FlashcardDto.FlashcardResponse(
                flashcard.flashcardId().uuid().toString(),
                flashcard.question().value(),
                flashcard.answer().value());
    }
}
