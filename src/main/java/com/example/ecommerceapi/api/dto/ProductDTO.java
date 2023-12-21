package com.example.ecommerceapi.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for product data.
 *
 * This class is used to transfer product data between different layers of the application,
 * particularly between the persistence layer and the client-facing controller. It can be tailored
 * to contain only the data necessary for client interactions, thus avoiding direct exposure
 * of entity models.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public class ProductDTO {

    /**
     * The unique identifier of the product.
     */
    private Long myProductID;

    /**
     * The name of the product.
     * It must not be blank and must be between 2 and 100 characters long.
     */
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String myProductName;

    /**
     * The price of the product.
     * It must not be null and must be a positive value.
     */
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be a positive value")
    private BigDecimal myPrice;

    /**
     * The quantity of the product available in stock.
     * It must not be less than 0.
     */
    @Min(value = 0, message = "Stock quantity cannot be less than 0")
    private int myStockQuantity;

    /**
     * Default no-argument constructor.
     * Used by frameworks and libraries for object instantiation.
     */
    public ProductDTO() {
        // no arg constructor
    }

    /**
     * Constructs a new ProductDTO with the specified parameters.
     *
     * @param theProductID (The unique identifier for the product)
     * @param theProductName (The name of the product)
     * @param thePrice (The price for the product)
     * @param theStockQuantity (The number of specified products in stock)
     */
    public ProductDTO(final Long theProductID, final String theProductName, final BigDecimal thePrice, final int theStockQuantity) {
        this.myProductID = theProductID;
        this.myProductName = theProductName;
        this.myPrice = thePrice;
        this.myStockQuantity = theStockQuantity;
    }

    /**
     * Gets the product's unique identifier.
     *
     * @return the unique identifier of the product.
     */
    public Long getProductID() {
        return myProductID;
    }

    /**
     * Sets the product's unique identifier.
     *
     * @param theProductID the new unique identifier for the product.
     */
    public void setProductID(final Long theProductID) {
        this.myProductID = theProductID;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product.
     */
    public String getProductName() {
        return myProductName;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product.
     */
    public void setProductName(final String theProductName) {
        this.myProductName = theProductName;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product.
     */
    public BigDecimal getPrice() {
        return myPrice;
    }

    /**
     * Sets the price of the product.
     *
     * @param thePrice the new price for the product.
     */
    public void setPrice(final BigDecimal thePrice) {
        this.myPrice = thePrice;
    }

    /**
     * Gets the stock quantity of the product.
     *
     * @return the stock quantity of the product.
     */
    public int getStockQuantity() {
        return myStockQuantity;
    }

    /**
     * Sets the stock quantity of the product.
     *
     * @param theStockQuantity the new stock quantity for the product.
     */
    public void setStockQuantity(final int theStockQuantity) {
        this.myStockQuantity = theStockQuantity;
    }
}
