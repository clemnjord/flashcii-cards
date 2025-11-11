package com.clemnjord.flashcii.web.config;

import com.clemnjord.flashcii.spring.shared.config.SharedSpringConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SharedSpringConfiguration.class)
public class ApplicationConfiguration {}
