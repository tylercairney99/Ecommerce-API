package com.example.ecommerceapi.api.repository;

import com.example.ecommerceapi.api.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
