package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long orderID;

    private LocalDateTime orderDate;

    private String status;

    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID") // Foreign key relationship
    private User user; // Object reference

    public Order() {
        // no arg constructor for JPA
    }

    public Order(final LocalDateTime theOrderDate, final String theStatus, final BigDecimal theTotalPrice, final User theUser) {
        this.orderDate = theOrderDate;
        this.status = theStatus;
        this.totalPrice = theTotalPrice;
        this.user = theUser;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final LocalDateTime theOrderDate) {
        this.orderDate = theOrderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String theStatus) {
        this.status = theStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final BigDecimal theTotalPrice) {
        this.totalPrice = theTotalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User theUser) {
        this.user = theUser;
    }
}
