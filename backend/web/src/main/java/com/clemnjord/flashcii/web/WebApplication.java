package com.clemnjord.flashcii.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "Flashcii API",
                        version = "1.0",
                        description = "API documentation for managing Flashcii resources"))
@SpringBootApplication(scanBasePackages = "com.clemnjord.flashcii")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
