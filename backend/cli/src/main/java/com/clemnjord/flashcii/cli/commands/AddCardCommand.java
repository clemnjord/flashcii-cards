package com.clemnjord.flashcii.cli.commands;

import com.clemnjord.flashcii.application.port.input.ICreateCardUseCase;
import com.clemnjord.flashcii.domain.model.Card;
import com.clemnjord.flashcii.domain.model.CollectionId;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.UUID;

@Command(name = "add", description = "Add a new flashcard.")
public class AddCardCommand implements Runnable {

    private final ICreateCardUseCase createCardUseCase;

    @CommandLine.Parameters(index = "0", description = "The question of the flashcard.")
    private String question;

    @CommandLine.Parameters(index = "1", description = "The answer of the flashcard.")
    private String answer;

    @CommandLine.Parameters(
            index = "2",
            description = "The UUID of the collection to add the card to.")
    private UUID collectionUUID;

    @Autowired
    public AddCardCommand(ICreateCardUseCase createCardUseCase) {
        this.createCardUseCase = createCardUseCase;
    }

    @Override
    public void run() {
        System.out.println("Adding a new flashcard...");

        var command =
                new ICreateCardUseCase.CreateCardCommand(
                        new CollectionId(collectionUUID), question, answer);
        Card newCard = createCardUseCase.execute(command);

        System.out.println("Card added successfully: " + newCard);
    }
}
