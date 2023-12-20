package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Product entity.
 * Extends JpaRepository to provide basic CRUD operations for Product entities.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
