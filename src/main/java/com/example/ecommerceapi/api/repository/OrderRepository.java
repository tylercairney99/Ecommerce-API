package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
