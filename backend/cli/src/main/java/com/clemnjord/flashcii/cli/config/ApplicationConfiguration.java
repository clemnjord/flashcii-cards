package com.clemnjord.flashcii.cli.config;

import com.clemnjord.flashcii.spring.config.SharedSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SharedSpringConfiguration.class)
public class ApplicationConfiguration {
}

