package com.clemnjord.flashcii.cli.commands;

import com.clemnjord.flashcii.application.port.input.deck.ICreateDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;

@Component
@Command(name = "create", description = "Create a new deck.")
public class CreateDeckCommand implements Runnable {

    private final ICreateDeckUseCase createDeckUseCase;

    @CommandLine.Parameters(index = "0", description = "Name of the deck")
    private String name;

    @CommandLine.Parameters(index = "1", description = "Description of the deck")
    private String description;

    public CreateDeckCommand(ICreateDeckUseCase createDeckUseCase) {
        this.createDeckUseCase = createDeckUseCase;
    }

    @Override
    public void run() {
        System.out.println("Adding a new deck...");

        var command = new com.clemnjord.flashcii.application.port.input.deck.CreateDeckCommand(name, description, new ArrayList<>());
        Deck newDeck = createDeckUseCase.execute(command);

        System.out.println("Deck added successfully: " + newDeck);
    }
}
