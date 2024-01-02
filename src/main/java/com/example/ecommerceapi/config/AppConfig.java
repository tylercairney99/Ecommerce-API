package com.example.ecommerceapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.dto.OrderLineDTO;

/**
 * Configuration class for setting up application-wide beans and settings.
 * This class is annotated with @Configuration to indicate that it contains
 * bean definitions for the Spring application context.
 *
 * This class provides a ModelMapper bean for mapping between domain objects and DTOs.
 * ModelMapper facilitates object mapping and reduces the amount of boilerplate code needed for conversions.
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
     * @return A new instance of ModelMapper configured with custom type mappings.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping for Order and OrderDTO
        modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(Order::getOrderLines, OrderDTO::setOrderLines);
            mapper.map(Order::getTotalPrice, OrderDTO::setTotalAmount); // Map total price to total amount
            // other mappings if needed...
        });

        // Mapping for OrderLine and OrderLineDTO
        modelMapper.typeMap(OrderLine.class, OrderLineDTO.class).addMappings(mapper -> {
            mapper.map(OrderLine::getPrice, OrderLineDTO::setUnitPrice);
            mapper.map(OrderLine::getQuantity, OrderLineDTO::setQuantity);
            // No direct mapping for lineTotal as it's a derived property
        });

        return modelMapper;
    }
}
