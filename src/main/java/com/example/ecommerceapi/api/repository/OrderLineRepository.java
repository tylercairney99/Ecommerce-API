package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for OrderLine entity.
 * Extends JpaRepository to provide basic CRUD operations for OrderLine entities.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
