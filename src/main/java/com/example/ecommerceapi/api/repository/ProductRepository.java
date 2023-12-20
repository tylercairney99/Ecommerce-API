package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
