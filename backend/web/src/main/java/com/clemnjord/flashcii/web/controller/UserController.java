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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "User management operations")
public class UserController {

    private final CreateFlashcardUseCase createFlashcardUseCase;

    @PostMapping("/{userId}/flashcards")
    @Operation(summary = "Create flashcard", description = "Create a flashcard")
    @ApiResponse(responseCode = "201", description = "Flashcard created successfully")
    @ApiResponse(responseCode = "403", description = "User not authorized")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ResponseStatus(HttpStatus.CREATED)
    public FlashcardDto.FlashcardResponse createFlashcard(
            @PathVariable String userId,
            @Valid @RequestBody FlashcardDto.FlashcardCreateRequest flashcardCreateRequest) {
        Flashcard flashcard = createFlashcardUseCase.execute(new CreateFlashcardCommand(
                UserId.from(userId),
                new Question(flashcardCreateRequest.question()),
                new Answer(flashcardCreateRequest.answer())));
        return new FlashcardDto.FlashcardResponse(
                flashcard.flashcardId().uuid().toString(),
                flashcard.question().value(),
                flashcard.answer().value());
    }
}
