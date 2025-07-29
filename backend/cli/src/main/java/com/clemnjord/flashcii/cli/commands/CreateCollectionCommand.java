package com.clemnjord.flashcii.cli.commands;

import com.clemnjord.flashcii.application.port.input.ICreateCollectionUseCase;
import com.clemnjord.flashcii.domain.model.Collection;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.ArrayList;

@Component
@Command(name = "create", description = "Create a new collection.")
public class CreateCollectionCommand implements Runnable {

    private final ICreateCollectionUseCase createCollectionUseCase;

    @CommandLine.Parameters(index = "0", description = "Name of the collection")
    private String name;

    @CommandLine.Parameters(index = "1", description = "Description of the collection")
    private String description;

    public CreateCollectionCommand(ICreateCollectionUseCase createCollectionUseCase) {
        this.createCollectionUseCase = createCollectionUseCase;
    }

    @Override
    public void run() {
        System.out.println("Adding a new collection...");

        var command =
                new ICreateCollectionUseCase.CreateCollectionCommand(name, description, new ArrayList<>());
        Collection newCollection = createCollectionUseCase.execute(command);

        System.out.println("Collection added successfully: " + newCollection);
    }
}
