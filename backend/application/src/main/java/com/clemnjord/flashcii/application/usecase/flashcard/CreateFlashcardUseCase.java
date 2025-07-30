package com.clemnjord.flashcii.application.usecase.flashcard;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import com.clemnjord.flashcii.application.annotation.ApplicationTransactional;
import com.clemnjord.flashcii.application.port.input.flashcard.ICreateFlashcardUseCase;
import com.clemnjord.flashcii.application.port.output.IDeckRepository;
import com.clemnjord.flashcii.application.port.output.IFlashcardRepository;
import com.clemnjord.flashcii.domain.exception.deck.DeckNotFoundException;
import com.clemnjord.flashcii.domain.exception.flashcard.FlashcardAlreadyExistsException;
import com.clemnjord.flashcii.domain.model.flashcard.Answer;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import com.clemnjord.flashcii.domain.model.flashcard.Question;

@ApplicationService
@ApplicationTransactional
public class CreateFlashcardUseCase implements ICreateFlashcardUseCase {
  private final IFlashcardRepository flashcardRepository;
  private final IDeckRepository deckRepository;

  public CreateFlashcardUseCase(
          IFlashcardRepository flashcardRepository, IDeckRepository deckRepository) {
    this.flashcardRepository = flashcardRepository;
    this.deckRepository = deckRepository;
  }

  @Override
  public Flashcard execute(CreateFlashcardCommand command) {
    // Check if the deck exists
    var deck =
            deckRepository
                    .findById(command.deckId())
            .orElseThrow(
                () ->
                        new DeckNotFoundException(
                                "Deck not found with ID: " + command.deckId().uuid()));

    // Check if flashcard already exists in the deck
    if (flashcardRepository.existsByQuestionAndDeckId(
            new Question(command.question()), command.deckId())) {
      throw new FlashcardAlreadyExistsException(
              "A flashcard with this question already exists in the deck");
    }

    // Create and save the new flashcard
    Flashcard flashcard = new Flashcard(null, new Question(command.question()), new Answer(command.answer()));
    Flashcard savedFlashcard = flashcardRepository.save(flashcard);

    // Associate the saved flashcard's ID to the deck
    deck.addFlashcard(savedFlashcard.flashcardId());

    // Save the updated deck
    deckRepository.save(deck);

    // Return the saved flashcard
    return savedFlashcard;
  }
}
