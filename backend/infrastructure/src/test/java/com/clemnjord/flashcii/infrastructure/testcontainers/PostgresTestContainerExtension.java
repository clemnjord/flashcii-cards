package com.clemnjord.flashcii.infrastructure.testcontainers;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.postgresql.PostgreSQLContainer;

public class PostgresTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    private static final PostgreSQLContainer postgres;

    static {
        postgres = new PostgreSQLContainer("postgres:18.1-alpine")
                .withDatabaseName("integration-tests-db")
                .withUsername("sa")
                .withPassword("sa")
                .withReuse(true);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // Container will be reused, so we don't stop it
    }

    public static PostgreSQLContainer getContainer() {
        return postgres;
    }
}
