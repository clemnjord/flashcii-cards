package com.clemnjord.flashcii.infrastructure.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.clemnjord.flashcii.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.clemnjord.flashcii.infrastructure.persistence.entity")
@ComponentScan(
        basePackages = {
            "com.clemnjord.flashcii.infrastructure.persistence.repository",
            "com.clemnjord.flashcii.infrastructure.persistence.mapper"
        })
public class TestJpaConfiguration {
    // This class is only used for testing
}
