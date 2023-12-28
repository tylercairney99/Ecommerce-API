package com.example.ecommerceapi.integration;

import com.example.ecommerceapi.api.dto.ProductDTO;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.OrderLineRepository;
import com.example.ecommerceapi.api.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;


import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Indicates the test should load the entire application for a more comprehensive integration test
@AutoConfigureMockMvc // Automatically configures myMockMvc for sending HTTP requests to controllers
public class ProductIntegrationTest {

    /**
     * MockMvc provides support for Spring MVC testing. It allows us to send HTTP requests
     * and assert the response status and content for integration testing.
     */
    @Autowired // Injects the MockMvc instance provided by Spring for simulating HTTP requests
    private MockMvc myMockMvc;

    /**
     * ObjectMapper is used for mapping JSON data to and from POJO (Plain Old Java Object)
     * for the purposes of testing REST controllers.
     */
    @Autowired // Injects the ObjectMapper instance for JSON serialization/deserialization
    private ObjectMapper myObjectMapper;

    /**
     * ProductRepository is the JPA repository for product entities. Used here to
     * directly interact with the database for test setup and verification.
     */
    @Autowired // Injects the ProductRepository for database operations related to 'Product'
    private ProductRepository myProductRepository;

    /**
     * OrderLineRepository is the JPA repository for order line entities. Used here to
     * clear any dependencies before deleting products in the setup phase.
     */
    @Autowired // Executed before each test. It clears the data from 'order_lines' and 'products' tables
    private OrderLineRepository myOrderLineRepository;

    /**
     * Set up method executed before each test. It ensures that the database is in a
     * known state by clearing the order lines and products tables. This is crucial for
     * maintaining test isolation and ensuring that tests do not interfere with each other.
     */
    @BeforeEach
    public void setUp() {
        myOrderLineRepository.deleteAll(); // Clear order lines before products
        myProductRepository.deleteAll(); // Now it's safe to delete all products
    }

    /**
     * Tests creating a new product. Verifies if the product is successfully created
     * and returns the expected status and data.
     */
    @Test
    @Transactional // Ensures that database operations in the test are rolled back after the test
    public void whenAddProduct_thenProductIsCreated() throws Exception {
        final ProductDTO productDTO = new ProductDTO(null, "Test Product",
                new BigDecimal("19.99"), 10);

        myMockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    /**
     * Tests retrieving all products. Verifies if the response contains the correct
     * product data and status.
     */
    @Test
    @Transactional
    public void whenGetAllProducts_thenProductsAreReturned() throws Exception {
        final Product savedProduct = myProductRepository.save(new Product("Test Product",
                new BigDecimal("19.99"), 10));

        myMockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value(savedProduct.getProductName()));
    }

    /**
     * Tests updating an existing product. Verifies if the product is successfully
     * updated and returns the correct status and updated data.
     */
    @Test
    @Transactional
    public void whenUpdateProduct_thenProductIsUpdated() throws Exception {
        final Product savedProduct = myProductRepository.save(new Product("Old Product",
                new BigDecimal("19.99"), 10));
        final ProductDTO updatedProductDTO = new ProductDTO(savedProduct.getID(), "Updated Product",
                new BigDecimal("29.99"), 20);

        myMockMvc.perform(put("/products/" + savedProduct.getID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(myObjectMapper.writeValueAsString(updatedProductDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Product"));
    }

    /**
     * Tests deleting an existing product. Verifies if the product is successfully
     * deleted and returns the expected status.
     */
    @Test
    @Transactional
    public void whenDeleteProduct_thenProductIsDeleted() throws Exception {
        final Product savedProduct = myProductRepository.save(new Product("Test Product",
                new BigDecimal("19.99"), 10));

        myMockMvc.perform(delete("/products/" + savedProduct.getID()))
                .andExpect(status().isNoContent());

        assertFalse(myProductRepository.findById(savedProduct.getID()).isPresent(),"Product should be deleted");
    }

    /**
     * Tests retrieving a specific product by its ID. Verifies if the correct product
     * is returned with the expected data.
     */
    @Test
    @Transactional
    public void whenGetProductByID_thenProductIsReturned() throws Exception {
        final Product savedProduct = myProductRepository.save(new Product("Test Product",
                new BigDecimal("19.99"), 10));

        myMockMvc.perform(get("/products/" + savedProduct.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(savedProduct.getProductName()));
    }

    /**
     * Tests retrieving a non-existent product by ID. Verifies if the response
     * returns a Not Found status.
     */
    @Test
    @Transactional
    public void whenGetNonExistentProduct_thenNotFound() throws Exception {
        final Long nonExistentProductID = 999L;

        myMockMvc.perform(get("/products/" + nonExistentProductID))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests updating a non-existent product. Verifies if the response returns
     * a Not Found status when trying to update a product that does not exist.
     */
    @Test
    @Transactional
    public void whenUpdateNonExistentProduct_thenNotFound() throws Exception {
        final ProductDTO updatedProductDTO = new ProductDTO(999L, "Non-Existent Product",
                new BigDecimal("29.99"), 20);

        myMockMvc.perform(put("/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(updatedProductDTO)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests deleting a non-existent product. Verifies if the response returns
     * a Not Found status when trying to delete a product that does not exist.
     */
    @Test
    @Transactional
    public void whenDeleteNonExistentProduct_thenNotFound() throws Exception {
        final Long nonExistentProductId = 999L;

        myMockMvc.perform(delete("/products/" + nonExistentProductId))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests adding a product with invalid data. Verifies if the response returns
     * a Bad Request status when product data does not meet validation criteria.
     */
    @Test
    @Transactional
    public void whenAddProductWithInvalidData_thenBadRequest() throws Exception {
        final ProductDTO invalidProductDTO = new ProductDTO(null, "",
                new BigDecimal("-19.99"), -10);

        myMockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myObjectMapper.writeValueAsString(invalidProductDTO)))
                .andExpect(status().isBadRequest());
    }
}
