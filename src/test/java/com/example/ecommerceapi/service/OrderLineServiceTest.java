package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.repository.OrderLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The ProductServiceTest class contains tests for the ProductService class.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class OrderLineServiceTest {

    /**
     * A mock version of OrderLineRepository for use in our tests without needing a real database.
     */
    @Mock
    private OrderLineRepository myOrderLineRepository;

    /**
     * The OrderLineService instance with the injected mock OrderLineRepository.
     */
    @InjectMocks
    private OrderLineService myOrderLineService;

    /**
     * A sample order line used for test cases.
     */
    private OrderLine myOrderLine;

    /**
     * A sample order line used for test cases.
     */
    private Order myOrder;

    /**
     * A sample product line used for test cases.
     */
    private Product myProduct;

    /**
     * Sets up a dummy order line for testing before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize a dummy orderLine for use in tests
        myOrder = new Order(); // Mock Order setup
        myProduct = new Product(); // Mock Product setup
        myOrderLine = new OrderLine(myOrder, myProduct, 10, BigDecimal.valueOf(100.00));
        myOrderLine.setOrderLineID(1L);
    }

    /**
     * Tests that adding an order line results in saving the order line.
     */
    @Test
    void whenAddOrderLine_thenSaveOrderLine() {
        // Arrange
        when(myOrderLineRepository.save(any(OrderLine.class))).thenReturn(myOrderLine);

        // Act
        OrderLine savedOrderLine = myOrderLineService.addOrderLine(myOrderLine);

        // Assert
        assertNotNull(savedOrderLine, "Saved order line should not be null");
        assertEquals(myOrderLine.getOrderLineID(), savedOrderLine.getOrderLineID(), "Saved order line ID should match the added order line ID");
        verify(myOrderLineRepository).save(myOrderLine);
    }

    /**
     * Tests that retrieving all order lines returns a non-empty list.
     */
    @Test
    void whenGetAllOrderLines_thenOrderLineList() {
        // Arrange
        when(myOrderLineRepository.findAll()).thenReturn(Arrays.asList(myOrderLine));

        // Act
        List<OrderLine> orderLines = myOrderLineService.getAllOrderLines();

        // Assert
        assertFalse(orderLines.isEmpty(), "Order line list should not be empty");
        assertEquals(1, orderLines.size(), "There should be one order line in the list");
        verify(myOrderLineRepository).findAll();
    }

    /**
     * Tests that retrieving an order line by ID returns an Optional containing the order line.
     */
    @Test
    void whenGetOrderLineByID_thenOrderLineOptional() {
        // Arrange
        when(myOrderLineRepository.findById(myOrderLine.getOrderLineID())).thenReturn(Optional.of(myOrderLine));

        // Act
        Optional<OrderLine> foundOrderLine = myOrderLineService.getOrderLineByID(myOrderLine.getOrderLineID());

        // Assert
        assertTrue(foundOrderLine.isPresent(), "Order line should be found for given ID");
        assertEquals(myOrderLine.getOrderLineID(), foundOrderLine.get().getOrderLineID(), "Found order line ID should match the query ID");
        verify(myOrderLineRepository).findById(myOrderLine.getOrderLineID());
    }

    /**
     * Tests that updating an order line results in the order line being correctly updated.
     */
    @Test
    void whenUpdateOrderLine_thenUpdatedOrderLine() {
        // Arrange
        when(myOrderLineRepository.findById(myOrderLine.getOrderLineID())).thenReturn(Optional.of(myOrderLine));
        when(myOrderLineRepository.save(any(OrderLine.class))).thenReturn(myOrderLine);

        // Act
        OrderLine updatedOrderLine = myOrderLineService.updateOrderLine(myOrderLine.getOrderLineID(), myOrderLine);

        // Assert
        assertNotNull(updatedOrderLine, "Updated order line should not be null");
        assertEquals(myOrderLine.getOrderLineID(), updatedOrderLine.getOrderLineID(), "Updated order line ID should match the original ID");
        verify(myOrderLineRepository).save(myOrderLine);
        verify(myOrderLineRepository).findById(myOrderLine.getOrderLineID());
    }

    /**
     * Tests that deleting an order line results in the order line being deleted.
     */
    @Test
    void whenDeleteOrderLine_thenOrderLineDeleted() {
        // Arrange
        when(myOrderLineRepository.findById(myOrderLine.getOrderLineID())).thenReturn(Optional.of(myOrderLine));
        doNothing().when(myOrderLineRepository).delete(myOrderLine);

        // Act & Assert
        assertDoesNotThrow(() -> myOrderLineService.deleteOrderLine(myOrderLine.getOrderLineID()), "Deleting an order line should not throw an exception");
        verify(myOrderLineRepository).delete(myOrderLine);
    }

    /**
     * Tests that attempting to update a non-existent order line throws an exception.
     */
    @Test
    void whenUpdateNonExistentOrderLine_thenThrowException() {
        // Arrange
        when(myOrderLineRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> myOrderLineService.updateOrderLine(999L, myOrderLine),
                "Updating a non-existent order line should throw ResponseStatusException");
        verify(myOrderLineRepository, never()).save(any(OrderLine.class));
    }

    /**
     * Tests that attempting to add a null order line throws an IllegalArgumentException.
     */
    @Test
    void whenAddNullOrderLine_thenIllegalArgumentExceptionIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> myOrderLineService.addOrderLine(null),
                "Adding a null order line should throw IllegalArgumentException");
    }

    /**
     * Tests that attempting to update an order line with an invalid quantity throws an IllegalArgumentException.
     */
    @Test
    void whenUpdateOrderLineWithInvalidQuantity_thenIllegalArgumentExceptionIsThrown() {
        myOrderLine.setQuantity(-5);
        assertThrows(IllegalArgumentException.class, () -> myOrderLineService.updateOrderLine(myOrderLine.getOrderLineID(), myOrderLine),
                "Updating an order line with negative quantity should throw IllegalArgumentException");
    }

    /**
     * Tests that attempting to add an order line with invalid data throws an IllegalArgumentException.
     */
    @Test
    void whenAddOrderLineWithInvalidData_thenIllegalArgumentExceptionIsThrown() {
        OrderLine invalidOrderLine = new OrderLine(myOrder, myProduct, -1, BigDecimal.valueOf(-100.00));
        assertThrows(IllegalArgumentException.class, () -> myOrderLineService.addOrderLine(invalidOrderLine),
                "Adding an order line with invalid data should throw IllegalArgumentException");
    }

    /**
     * Tests that attempting to update an order line with a non-existent product or order throws an exception.
     */
    @Test
    void whenUpdateOrderLineWithNonExistentProductOrOrder_thenThrowException() {
        OrderLine updatedOrderLine = new OrderLine(new Order(), new Product(), 10, BigDecimal.valueOf(200.00));
        updatedOrderLine.setOrderLineID(myOrderLine.getOrderLineID());
        assertThrows(ResponseStatusException.class, () -> myOrderLineService.updateOrderLine(updatedOrderLine.getOrderLineID(), updatedOrderLine),
                "Updating an order line with non-existent product or order should throw exception");
    }

    /**
     * Tests that attempting to delete a non-existent order line throws an exception.
     */
    @Test
    void whenDeleteNonExistentOrderLine_thenThrowException() {
        assertThrows(ResponseStatusException.class, () -> myOrderLineService.deleteOrderLine(999L),
                "Deleting a non-existent order line should throw exception");
    }

    /**
     * Tests that retrieving all order lines when none exist results in an empty list.
     */
    @Test
    void whenGetAllOrderLinesAndNoneExist_thenEmptyList() {
        when(myOrderLineRepository.findAll()).thenReturn(Collections.emptyList());
        List<OrderLine> orderLines = myOrderLineService.getAllOrderLines();
        assertTrue(orderLines.isEmpty(), "Retrieving all order lines should return an empty list if none exist");
    }

    /**
     * Tests that updating an order line with changed product or order correctly updates the order line.
     */
    @Test
    void whenUpdateOrderLineWithChangedProductOrOrder_thenUpdatedCorrectly() {
        Order newOrder = new Order(); // Mock new Order setup
        Product newProduct = new Product(); // Mock new Product setup
        OrderLine updatedOrderLine = new OrderLine(newOrder, newProduct, 15, BigDecimal.valueOf(300.00));
        updatedOrderLine.setOrderLineID(myOrderLine.getOrderLineID());

        when(myOrderLineRepository.findById(updatedOrderLine.getOrderLineID())).thenReturn(Optional.of(myOrderLine));
        when(myOrderLineRepository.save(any(OrderLine.class))).thenReturn(updatedOrderLine);

        OrderLine result = myOrderLineService.updateOrderLine(updatedOrderLine.getOrderLineID(), updatedOrderLine);
        assertNotNull(result, "Updated order line should not be null");
        assertEquals(newOrder, result.getOrder(), "Order should be updated");
        assertEquals(newProduct, result.getProduct(), "Product should be updated");
    }
}
