package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Order entity.
 * Extends JpaRepository to provide basic CRUD operations for Order entities.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
