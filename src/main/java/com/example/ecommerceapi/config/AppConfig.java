package com.example.ecommerceapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up application-wide beans and settings.
 * This class is annotated with @Configuration to indicate that it contains
 * bean definitions for the Spring application context.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@Configuration
public class AppConfig {

    /**
     * Creates a ModelMapper bean to be used throughout the application for mapping
     * between domain objects and DTOs. The ModelMapper facilitates object mapping
     * and reduces the amount of boilerplate code needed for conversions.
     *
     * @return A new instance of ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
