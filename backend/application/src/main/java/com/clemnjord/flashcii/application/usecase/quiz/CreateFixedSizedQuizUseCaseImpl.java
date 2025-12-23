package com.clemnjord.flashcii.application.usecase.quiz;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizCommand;
import com.clemnjord.flashcii.application.port.input.quiz.CreateFixedSizedQuizUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFixedSizedQuizRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.application.port.output.IUserContextService;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.FlashcardId;
import com.clemnjord.flashcii.domain.model.quiz.FixedSizedQuiz;
import com.clemnjord.flashcii.domain.model.quiz.QuizId;
import com.clemnjord.flashcii.domain.model.user.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@ApplicationService
@ApplicationTransactional
@AllArgsConstructor
public class CreateFixedSizedQuizUseCaseImpl implements CreateFixedSizedQuizUseCase {

    private final IFixedSizedQuizRepository quizRepository;
    private final IUserContextService userContextService;
    private final IDeckRepository deckRepository;
    private final IFlashcardRepository flashcardRepository;

    @Override
    public FixedSizedQuiz execute(CreateFixedSizedQuizCommand command) {
        User currentUser = userContextService.getCurrentUser();

        // Filter out decks that are not owned by the current user
        Set<DeckId> decks = command.includedDecks().stream()
                .map(deckId -> deckRepository.findByIdAndOwnerId(deckId, currentUser.userId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Deck::deckId)
                .collect(Collectors.toSet());

        // Get due flashcards from decks
        List<FlashcardId> flashcardIds =
                flashcardRepository.findDueFlashcardsByDeckIdsAndOwnerId(decks, currentUser.userId()).stream()
                        .map(Flashcard::flashcardId)
                        .toList();

        if (flashcardIds.isEmpty()) {
            throw new IllegalStateException("Cannot create quiz: no due flashcards found for the selected decks.");
        }

        // Create and persist quiz
        FixedSizedQuiz quiz = new FixedSizedQuiz(QuizId.generate(), currentUser.userId(), flashcardIds);
        quizRepository.save(quiz, currentUser.userId());
        return quiz;
    }
}
