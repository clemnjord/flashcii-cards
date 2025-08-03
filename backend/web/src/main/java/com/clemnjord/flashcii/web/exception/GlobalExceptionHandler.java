package com.clemnjord.flashcii.web.exception;

import com.clemnjord.flashcii.domain.exception.deck.DeckAlreadyExistsException;
import com.clemnjord.flashcii.domain.exception.deck.InvalidDeckException;
import com.clemnjord.flashcii.domain.exception.user.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeckAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleDeckAlreadyExists(DeckAlreadyExistsException ex) {
        return new ApiErrorResponse(
                "DECK_ALREADY_EXISTS",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return new ApiErrorResponse(
                "USER_ALREADY_EXISTS",
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
    }

    @ExceptionHandler(InvalidDeckException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidDeck(InvalidDeckException ex) {
        return new ApiErrorResponse(
                "INVALID_DECK",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return new ValidationErrorResponse(
                "VALIDATION_FAILED",
                "Request validation failed",
                fieldErrors,
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );
    }

    public record ApiErrorResponse(String errorCode, String message, int status, Instant timestamp) {
    }

    public record ValidationErrorResponse(
            String errorCode,
            String message,
            Map<String, String> fieldErrors,
            int status,
            Instant timestamp
    ) {
    }

}