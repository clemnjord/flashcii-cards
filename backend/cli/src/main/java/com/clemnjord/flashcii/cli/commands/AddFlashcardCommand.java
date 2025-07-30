package com.clemnjord.flashcii.cli.commands;

import com.clemnjord.flashcii.application.port.input.flashcard.ICreateFlashcardUseCase;
import com.clemnjord.flashcii.domain.model.deck.DeckId;
import com.clemnjord.flashcii.domain.model.flashcard.Flashcard;
import org.springframework.beans.factory.annotation.Autowired;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.UUID;

@Command(name = "add", description = "Add a new flashcard.")
public class AddFlashcardCommand implements Runnable {

    private final ICreateFlashcardUseCase createCardUseCase;

    @CommandLine.Parameters(index = "0", description = "The question of the flashcard.")
    private String question;

    @CommandLine.Parameters(index = "1", description = "The answer of the flashcard.")
    private String answer;

    @CommandLine.Parameters(
            index = "2",
            description = "The UUID of the deck to add the flashcard to.")
    private UUID deckUUID;

    @Autowired
    public AddFlashcardCommand(ICreateFlashcardUseCase createCardUseCase) {
        this.createCardUseCase = createCardUseCase;
    }

    @Override
    public void run() {
        System.out.println("Adding a new flashcard...");

        var command =
                new ICreateFlashcardUseCase.CreateFlashcardCommand(
                        new DeckId(deckUUID), question, answer);
        Flashcard newFlashcard = createCardUseCase.execute(command);

        System.out.println("Flashcard added successfully: " + newFlashcard);
    }
}
