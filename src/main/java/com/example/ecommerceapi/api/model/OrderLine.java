package com.example.ecommerceapi.api.model;


import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_lines")
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long orderLineID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderID")
    private Order order; // Order Object reference

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productID")
    private Product product; // Product Object reference

    private int quantity ;

    @Column(precision = 10, scale = 2) // Optional: specify precision and scale if needed
    private BigDecimal price;

    public OrderLine() {
        // no arg constructor for JPA
    }

    public OrderLine(final Order theOrder, final Product theProduct, final int theQuantity, final BigDecimal thePrice) {
        this.order = theOrder;
        this.product = theProduct;
        this.quantity = theQuantity;
        this.price = thePrice;
    }

    public Long getOrderLineID() {
        return orderLineID;
    }

    public void setOrderLineID(final Long theOderLineID) {
        this.orderLineID = theOderLineID;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order theOrder) {
        this.order = theOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product theProduct) {
        this.product = theProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int theQuantity) {
        this.quantity = theQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal thePrice) {
        this.price = thePrice;
    }
}
