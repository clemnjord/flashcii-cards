package com.clemnjord.flashcii.service.flashcard.usecase;

import com.clemnjord.flashcii.domain.flashcard.model.Card;
import com.clemnjord.flashcii.domain.flashcard.repository.ICardRepository;
import com.clemnjord.flashcii.domain.flashcard.repository.ICollectionRepository;
import com.clemnjord.flashcii.service.flashcard.command.CreateCardCommand;

public class CreateCardUseCase {
    private final ICardRepository cardRepository;
    private final ICollectionRepository cardCollectionRepository;

    public CreateCardUseCase(ICardRepository cardRepository,
                             ICollectionRepository cardCollectionRepository) {
        this.cardRepository = cardRepository;
        this.cardCollectionRepository = cardCollectionRepository;
    }

    public Card execute(CreateCardCommand command) {
        // Check if the collection exists
        var collection = cardCollectionRepository.findById(command.collectionId())
                .orElseThrow(() -> new IllegalArgumentException("Collection not found with ID: " + command.collectionId()));

        // Create a new card
        Card card = new Card(null, command.question(), command.answer());

        // Associate the card with the collection
        collection.addCard(card.cardId());

        // Save the card to the repository
        Card savedCard = cardRepository.save(card);

        // Save the updated collection
        cardCollectionRepository.save(collection);

        // Return the saved card
        return savedCard;
    }
}
