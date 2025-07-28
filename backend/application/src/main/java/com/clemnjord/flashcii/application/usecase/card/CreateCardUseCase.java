package com.clemnjord.flashcii.application.usecase.card;

import com.clemnjord.flashcii.application.port.input.ICreateCardUseCase;
import com.clemnjord.flashcii.application.port.output.ICardRepository;
import com.clemnjord.flashcii.application.port.output.ICollectionRepository;
import com.clemnjord.flashcii.domain.exception.card.CardAlreadyExistsException;
import com.clemnjord.flashcii.domain.exception.collection.CollectionNotFoundException;
import com.clemnjord.flashcii.domain.model.Answer;
import com.clemnjord.flashcii.domain.model.Card;
import com.clemnjord.flashcii.domain.model.Question;

public class CreateCardUseCase implements ICreateCardUseCase {
  private final ICardRepository cardRepository;
  private final ICollectionRepository cardCollectionRepository;

  public CreateCardUseCase(
      ICardRepository cardRepository, ICollectionRepository cardCollectionRepository) {
    this.cardRepository = cardRepository;
    this.cardCollectionRepository = cardCollectionRepository;
  }

  @Override
  public Card execute(CreateCardCommand command) {
    // Check if the collection exists
    var collection =
        cardCollectionRepository
            .findById(command.collectionId())
            .orElseThrow(
                () ->
                    new CollectionNotFoundException(
                        "Collection not found with ID: " + command.collectionId().uuid()));

    // Check if card already exists in the collection
    if (cardRepository.existsByQuestionAndCollectionId(
        command.question(), command.collectionId())) {
      throw new CardAlreadyExistsException(
          "A card with this question already exists in the collection");
    }

    // Create and save the new card
    Card card = new Card(null, new Question(command.question()), new Answer(command.answer()));
    Card savedCard = cardRepository.save(card);

    // Associate the saved card's ID to the collection
    collection.addCard(savedCard.cardId());

    // Save the updated collection
    cardCollectionRepository.save(collection);

    // Return the saved card
    return savedCard;
  }
}
