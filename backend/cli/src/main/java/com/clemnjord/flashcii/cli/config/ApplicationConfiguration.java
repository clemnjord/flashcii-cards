package com.clemnjord.flashcii.cli.config;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(
        basePackages = {"com.clemnjord.flashcii.application", "com.clemnjord.flashcii.infrastructure"},
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ANNOTATION,
                        classes = {ApplicationService.class})
        })
@EnableJpaRepositories(
        basePackages = "com.clemnjord.flashcii.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.clemnjord.flashcii.infrastructure.persistence.entity")
@EnableTransactionManagement
public class ApplicationConfiguration {

    // Custom aspect to map your annotation to Spring's @Transactional
    // Isn't that over colicated to avoid using Spring Transactional annotation?
    @Bean
    public TransactionalAnnotationAspect transactionalAnnotationAspect() {
        return new TransactionalAnnotationAspect();
    }
}

