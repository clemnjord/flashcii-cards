package com.clemnjord.flashcii.cli.config;

import com.clemnjord.flashcii.cli.RootCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;
import picocli.spring.PicocliSpringFactory;

@Configuration
public class CliApplicationConfig {

    private final ApplicationContext applicationContext;

    public CliApplicationConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            // Initialize the RootCommand using SpringFactory for subcommand DI
            CommandLine commandLine =
                    new CommandLine(
                            applicationContext.getBean(RootCommand.class),
                            new PicocliSpringFactory(applicationContext));

            // Execute the CLI with incoming arguments.
            int exitCode = commandLine.execute(args);

            // Optionally, exit with the CLI result code
            System.exit(exitCode);
        };
    }
}
