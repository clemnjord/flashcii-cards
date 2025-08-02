package com.clemnjord.flashcii.spring.shared.config;

import com.clemnjord.flashcii.application.annotation.ApplicationService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(
        basePackages = {
                "com.clemnjord.flashcii.application",
                "com.clemnjord.flashcii.infrastructure",
                "com.clemnjord.flashcii.spring.config"
        },
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ANNOTATION,
                        classes = {ApplicationService.class})
        })
@EnableJpaRepositories(
        basePackages = "com.clemnjord.flashcii.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.clemnjord.flashcii.infrastructure.persistence.entity")
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SharedSpringConfiguration {
}