package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
