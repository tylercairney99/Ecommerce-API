package com.example.ecommerceapi.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for order line data in the e-commerce application.
 *
 * This class is used to transfer order line data between different layers of the application,
 * particularly between the persistence layer and the client-facing controller. It encapsulates
 * details about an individual order line item, such as the product, quantity, and line total.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
public class OrderLineDTO {

    /**
     * The unique identifier of the product in the order line.
     */
    @NotNull(message = "Product ID is required")
    private Long productId;

    /**
     * The quantity of the product in the order line.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    /**
     * The price of the product per unit.
     */
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;

    /**
     * The total price for this order line item.
     */
    private BigDecimal lineTotal;

    // Default constructor
    public OrderLineDTO() {
    }

    // All-arguments constructor
    public OrderLineDTO(Long productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice.multiply(new BigDecimal(quantity));
    }

    // Getters and Setters with JavaDoc comments

    /**
     * Gets the product ID of the order line.
     *
     * @return the product ID.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the product ID of the order line.
     *
     * @param productId the new product ID.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Gets the quantity of the product in the order line.
     *
     * @return the quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the order line.
     *
     * @param quantity the new quantity.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Recalculate line total if quantity is set after initialization
        if (this.unitPrice != null) {
            this.lineTotal = this.unitPrice.multiply(new BigDecimal(quantity));
        }
    }

    /**
     * Gets the unit price of the product in the order line.
     *
     * @return the unit price.
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of the product in the order line.
     *
     * @param unitPrice the new unit price.
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        // Recalculate line total if unit price is set after initialization
        this.lineTotal = unitPrice.multiply(new BigDecimal(this.quantity));
    }

    /**
     * Gets the total price for this order line item.
     *
     * @return the line total.
     */
    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    // Note: Typically, there's no setter for lineTotal as it's derived from quantity and unit price

}
