package com.example.ecommerceapi.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String productName;

    private BigDecimal Price;

    private int StockQuantity;


    public Product() {
        // no arg constructor for JPA
    }

    public Product(final String theProductName, final BigDecimal thePrice, final int theStockQuantity) {
        this.productName = theProductName;
        this.Price = thePrice;
        this.StockQuantity = theStockQuantity;
    }

    public Long getID() {
        return id;
    }

    public void setID(final Long theID) {
        this.id = theID;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(final String theProductName) {
        this.productName = theProductName;
    }
    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(final BigDecimal thePrice) {
        this.Price = thePrice;
    }
    public int getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(final int theStockQuantity) {
        this.StockQuantity = theStockQuantity;
    }
}
