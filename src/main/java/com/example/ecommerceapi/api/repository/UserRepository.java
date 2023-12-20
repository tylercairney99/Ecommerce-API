package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide basic CRUD operations for User entities.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
