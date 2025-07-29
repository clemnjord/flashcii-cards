package com.clemnjord.flashcii.cli;

import com.clemnjord.flashcii.cli.commands.AddCardCommand;
import com.clemnjord.flashcii.cli.commands.CreateCollectionCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@Command(
        name = "flashcii-cli",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Flashcii CLI Application for managing flashcards.",
        subcommands = {AddCardCommand.class, CreateCollectionCommand.class})
public class RootCommand implements Runnable {

    @Option(
            names = {"-v", "--verbose"},
            description = "Enable verbose mode.")
    private boolean verbose;

    @Override
    public void run() {
        if (verbose) {
            System.out.println("Verbose mode is enabled.");
        } else {
            System.out.println("Welcome to Flashcii CLI!");
        }
    }
}
