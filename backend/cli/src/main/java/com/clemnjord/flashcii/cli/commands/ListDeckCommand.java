package com.clemnjord.flashcii.cli.commands;

import com.clemnjord.flashcii.application.port.input.deck.IListDeckUseCase;
import com.clemnjord.flashcii.domain.model.deck.Deck;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.List;

@Component
@Command(name = "list", description = "List decks for the current user.")
public class ListDeckCommand implements Runnable {

    private final IListDeckUseCase listDeckUseCase;

    @CommandLine.Parameters(index = "0", description = "Deck name filter", defaultValue = "")
    private String nameFilter;

    public ListDeckCommand(IListDeckUseCase listDeckUseCase) {
        this.listDeckUseCase = listDeckUseCase;
    }

    @Override
    public void run() {
        System.out.println("Listing decks...");

        var command =
                new com.clemnjord.flashcii.application.port.input.deck.ListDeckCommand(nameFilter);
        List<Deck> deckList = listDeckUseCase.execute(command);

        deckList.forEach(System.out::println);

        System.out.println("Deck listed successfully. " + deckList.size() + " decks found.");
    }
}
