package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The ProductServiceTest class contains tests for the ProductService class.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    /**
     * A mock version of ProductRepository for use in our tests without needing a real database.
     */
    @Mock
    private ProductRepository myProductRepository;

    /**
     * The ProductService instance with the injected mock ProductRepository.
     */
    @InjectMocks
    private ProductService myProductService;

    /**
     * A sample product used for test cases.
     */
    private Product myProduct;

    /**
     * Sets up a dummy product for testing before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize a dummy product for use in tests
        myProduct = new Product("Test Product", BigDecimal.valueOf(19.99), 100);
        myProduct.setID(1L); // Assuming there's a setter for the ID in your Product class
    }

    /**
     * Test adding a product results in the correct save operation.
     */
    @Test
    void whenAddProduct_thenSaveProduct() {
        // Arrange
        when(myProductRepository.save(any(Product.class))).thenReturn(myProduct);

        // Act
        Product savedProduct = myProductService.addProduct(myProduct);

        // Assert
        assertNotNull(savedProduct,"Saved product should not be null");
        assertEquals(myProduct.getProductName(), savedProduct.getProductName(),
                "Product name should match the saved product name");
        verify(myProductRepository).save(myProduct);
    }

    /**
     * Test retrieving all products gives the correct list.
     */
    @Test
    void whenGetAllProducts_thenProductList() {
        // Arrange
        when(myProductRepository.findAll()).thenReturn(Arrays.asList(myProduct));

        // Act
        List<Product> products = myProductService.getAllProducts();

        // Assert
        assertFalse(products.isEmpty(), "Product list should not be empty");
        assertEquals(1, products.size(), "List size should be 1");
        verify(myProductRepository).findAll();
    }

    /**
     * Test finding a product by ID returns the correct product.
     */
    @Test
    void whenGetProductByID_thenProductOptional() {
        // Arrange
        when(myProductRepository.findById(myProduct.getID())).thenReturn(Optional.of(myProduct));

        // Act
        Optional<Product> foundProduct = myProductService.getProductByID(myProduct.getID());

        // Assert
        assertTrue(foundProduct.isPresent(), "Product should be found with given ID");
        assertEquals(myProduct.getID(), foundProduct.get().getID(),
                "Product ID should match the found product ID");
        verify(myProductRepository).findById(myProduct.getID());
    }

    /**
     * Test updating a product results in the correct update operation.
     */
    @Test
    void whenUpdateProduct_thenUpdatedProduct() {
        // Arrange
        when(myProductRepository.findById(myProduct.getID())).thenReturn(Optional.of(myProduct));
        when(myProductRepository.save(any(Product.class))).thenReturn(myProduct);

        // Act
        Product updatedProduct = myProductService.updateProduct(myProduct.getID(), myProduct);

        // Assert
        assertNotNull(updatedProduct, "Updated product should not be null");
        assertEquals(myProduct.getProductName(), updatedProduct.getProductName(),
                "Product name should match the updated product name");
        verify(myProductRepository).save(myProduct);
        verify(myProductRepository).findById(myProduct.getID());
    }

    /**
     * Test deleting a product results in the correct delete operation.
     */
    @Test
    void whenDeleteProduct_thenProductDeleted() {
        // Arrange
        when(myProductRepository.findById(myProduct.getID())).thenReturn(Optional.of(myProduct));
        doNothing().when(myProductRepository).delete(myProduct);

        // Act & Assert
        assertDoesNotThrow(() -> myProductService.deleteProduct(myProduct.getID()),
                "Deleting a product should not throw an exception");
        verify(myProductRepository).delete(myProduct);
    }

    /**
     * Test updating a non-existent product throws the correct exception.
     */
    @Test
    void whenUpdateNonExistentProduct_thenThrowException() {
        // Arrange
        when(myProductRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> myProductService.updateProduct(999L, myProduct),
                "Updating a non-existent product should throw an exception");
        verify(myProductRepository, never()).save(any(Product.class));
    }

    /**
     * Test case to verify that when adding a product with all fields specified,
     * the added product's fields match the expected values.
     */
    @Test
    void whenAddProductWithAllFields_thenAllFieldsAreCorrect() {
        when(myProductRepository.save(any(Product.class))).thenReturn(myProduct);
        Product savedProduct = myProductService.addProduct(myProduct);
        assertNotNull(savedProduct, "Saved product should not be null");
        assertEquals(myProduct.getProductName(), savedProduct.getProductName(), "Product names should match");
        assertEquals(myProduct.getPrice(), savedProduct.getPrice(), "Prices should match");
        assertEquals(myProduct.getStockQuantity(), savedProduct.getStockQuantity(), "Stock quantities should match");
        verify(myProductRepository).save(myProduct);
    }

    /**
     * Test case to verify that when updating a product, all fields are correctly updated
     * to the new values.
     */
    @Test
    void whenUpdateProduct_thenAllFieldsAreUpdated() {
        Product updatedInfo = new Product("UpdatedProduct", BigDecimal.valueOf(29.99), 20);
        updatedInfo.setID(myProduct.getID());
        when(myProductRepository.findById(myProduct.getID())).thenReturn(Optional.of(myProduct));
        when(myProductRepository.save(any(Product.class))).thenReturn(updatedInfo);
        Product result = myProductService.updateProduct(updatedInfo.getID(), updatedInfo);
        assertNotNull(result, "Updated product should not be null");
        assertEquals(updatedInfo.getProductName(), result.getProductName(), "Product names should be updated");
        assertEquals(updatedInfo.getPrice(), result.getPrice(), "Prices should be updated");
        assertEquals(updatedInfo.getStockQuantity(), result.getStockQuantity(), "Stock quantities should be updated");
    }

    /**
     * Test case to verify that attempting to add a null product results in an
     * IllegalArgumentException being thrown.
     */
    @Test
    void whenAddNullProduct_thenIllegalArgumentExceptionIsThrown() {
        final Product nullProduct = null;
        assertThrows(IllegalArgumentException.class, () -> myProductService.addProduct(nullProduct),
                "Adding a null product should throw IllegalArgumentException");
    }

    /**
     * Test case to verify that attempting to update a product with invalid data
     * results in an IllegalArgumentException being thrown.
     */
    @Test
    void whenUpdateProductWithInvalidData_thenIllegalArgumentExceptionIsThrown() {
        Product invalidProduct = new Product("", BigDecimal.valueOf(-10), -5);
        invalidProduct.setID(myProduct.getID());
        assertThrows(IllegalArgumentException.class, () -> myProductService.updateProduct(invalidProduct.getID(), invalidProduct),
                "Updating a product with invalid data should throw IllegalArgumentException");
    }

    // Potentially add more tests for edge cases, but as of now there is full method coverage for ProductService

}