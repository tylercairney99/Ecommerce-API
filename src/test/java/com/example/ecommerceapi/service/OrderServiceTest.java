package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.dto.OrderDTO;
import com.example.ecommerceapi.api.dto.OrderLineDTO;
import com.example.ecommerceapi.api.model.OrderLine;
import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.model.Order;
import com.example.ecommerceapi.api.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    /**
     * A mock version of OrderRepository for use in our tests without needing a real database.
     */
    @Mock
    private OrderRepository myOrderRepository;

    /**
     * The OrderService instance with the injected mock OrderRepository.
     */
    @InjectMocks
    private OrderService myOrderService;

    /**
     * A sample order used for test cases.
     */
    private Order myOrder;

    /**
     * Set up a dummy order for testing.
     */

    /**
     * A date used for test cases.
     */
    private LocalDateTime myTestDate;

    /**
     * A static status used for test cases.
     */
    private String myTestStatus;

    /**
     * A set price used for test cases.
     */
    private BigDecimal myTestTotalPrice;

    /**
     * A dummy user used for test cases.
     */
    private User myTestUser;

    @Mock
    private ProductService myProductService;

    /**
     * Sets up a dummy order and user for testing before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize a dummy order for use in tests
        myTestDate = LocalDateTime.now();
        myTestStatus = "Processing";
        myTestTotalPrice = BigDecimal.valueOf(99.99);
        myTestUser = new User("username123", "password123", "test@gmail.com");

        myOrder = new Order(myTestDate, myTestStatus, myTestTotalPrice, myTestUser);
        myOrder.setOrderID(1L);
    }

    /**
     * Test adding an order saves correctly.
     */
    @Test
    void whenAddOrder_thenSaveOrder() {
        // Arrange
        when(myOrderRepository.save(any(Order.class))).thenReturn(myOrder);

        // Act
        Order savedOrder = myOrderService.addOrder(myOrder);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(myOrder.getOrderID(), savedOrder.getOrderID());
        verify(myOrderRepository).save(myOrder);
    }

    /**
     * Test retrieving all orders returns the correct list.
     */
    @Test
    void whenGetAllOrders_thenOrderList() {
        // Arrange
        when(myOrderRepository.findAll()).thenReturn(Arrays.asList(myOrder));

        // Act
        List<Order> orders = myOrderService.getAllOrders();

        // Assert
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        verify(myOrderRepository).findAll();
    }

    /**
     * Test finding an order by ID returns the correct order.
     */
    @Test
    void whenGetOrderByID_thenOrderOptional() {
        // Arrange
        when(myOrderRepository.findById(myOrder.getOrderID())).thenReturn(Optional.of(myOrder));

        // Act
        Optional<Order> foundOrder = myOrderService.getOrderByID(myOrder.getOrderID());

        // Assert
        assertTrue(foundOrder.isPresent());
        assertEquals(myOrder.getOrderID(), foundOrder.get().getOrderID());
        verify(myOrderRepository).findById(myOrder.getOrderID());
    }

    /**
     * Test updating an order results in correct update operation.
     */
    @Test
    void whenUpdateOrder_thenUpdatedOrder() {
        // Arrange
        when(myOrderRepository.findById(myOrder.getOrderID())).thenReturn(Optional.of(myOrder));
        when(myOrderRepository.save(any(Order.class))).thenReturn(myOrder);

        // Act
        Order updatedOrder = myOrderService.updateOrder(myOrder.getOrderID(), myOrder);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(myOrder.getOrderID(), updatedOrder.getOrderID());
        verify(myOrderRepository).save(myOrder);
        verify(myOrderRepository).findById(myOrder.getOrderID());
    }

    /**
     * Test deleting an order results in correct delete operation.
     */
    @Test
    void whenDeleteOrder_thenOrderDeleted() {
        // Arrange
        when(myOrderRepository.findById(myOrder.getOrderID())).thenReturn(Optional.of(myOrder));
        doNothing().when(myOrderRepository).delete(myOrder);

        // Act & Assert
        assertDoesNotThrow(() -> myOrderService.deleteOrder(myOrder.getOrderID()));
        verify(myOrderRepository).delete(myOrder);
    }

    /**
     * Test updating a non-existent order throws the expected exception.
     */
    @Test
    void whenUpdateNonExistentOrder_thenThrowException() {
        // Arrange
        when(myOrderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> myOrderService.updateOrder(999L, myOrder));
        verify(myOrderRepository, never()).save(any(Order.class));
    }

    /**
     * Tests that adding an order correctly saves all fields.
     */
    @Test
    void whenAddOrder_thenAllFieldsAreCorrect() {
        // Arrange
        when(myOrderRepository.save(any(Order.class))).thenReturn(myOrder);

        // Act
        Order savedOrder = myOrderService.addOrder(myOrder);

        // Assert
        assertNotNull(savedOrder, "Saved order should not be null");
        assertEquals(myOrder.getOrderID(), savedOrder.getOrderID(), "Order ID should match");
        assertEquals(myOrder.getOrderDate(), savedOrder.getOrderDate(), "Order date should match");
        assertEquals(myOrder.getStatus(), savedOrder.getStatus(), "Order status should match");
        assertEquals(myOrder.getTotalPrice(), savedOrder.getTotalPrice(), "Order total price should match");
        assertEquals(myOrder.getMyUser().getUserID(), savedOrder.getMyUser().getUserID(), "Order user should match");
        verify(myOrderRepository).save(myOrder);
    }

    /**
     * Tests that updating an order correctly updates all fields.
     */
    @Test
    void whenUpdateOrder_thenAllFieldsAreUpdated() {
        // Arrange
        Long orderId = 1L;
        User testUser = new User("TestUser", "Password123", "testuser@example.com");
        testUser.setUserID(1L);
        Product testProduct = new Product("ProductName", new BigDecimal("30.00"), 100);

        when(myProductService.getProductByID(testProduct.getID())).thenReturn(Optional.of(testProduct));


        // Creating a mock order and setting it up
        Order existingOrder = new Order(LocalDateTime.now(), "Created", new BigDecimal("100.00"), testUser);
        existingOrder.setOrderID(orderId);
        existingOrder.addOrderLine(new OrderLine(existingOrder, testProduct, 2, new BigDecimal("30.00"))); // Existing order line

        // Mocking repository and service responses
        when(myOrderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(myProductService.getProductByID(testProduct.getID())).thenReturn(Optional.of(testProduct));

        // Prepare updated information
        LocalDateTime updatedOrderDate = LocalDateTime.now().minusDays(1);
        BigDecimal updatedTotalPrice = new BigDecimal("150.00");
        String updatedStatus = "Shipped";

        // Create a new Order object with updated information
        Order updatedOrder = new Order(updatedOrderDate, updatedStatus, updatedTotalPrice, testUser);
        updatedOrder.setOrderID(orderId);
        updatedOrder.addOrderLine(new OrderLine(updatedOrder, testProduct, 5, new BigDecimal("30.00")));

        // Act
        myOrderService.updateOrder(orderId, updatedOrder);

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(myOrderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertNotNull(capturedOrder, "Updated order should not be null");
        assertEquals(updatedOrderDate, capturedOrder.getOrderDate(), "Order date should be updated");
        assertEquals(updatedTotalPrice, capturedOrder.getTotalPrice(), "Order total price should be updated");
        assertEquals(testUser.getUserID(), capturedOrder.getMyUser().getUserID(), "Order user should match");
        // Additional assertions for order lines can be added here if needed
    }


    /**
     * Tests that updating an order's associated user updates the user in the order.
     */
    @Test
    void whenUpdateOrderWithModifiedUser_thenUserIsUpdated() {
        // Arrange
        User newUser = new User("newUsername", "newPassword", "newEmail@gmail.com");
        Order updatedOrder = new Order(myTestDate, myTestStatus, myTestTotalPrice, newUser);
        updatedOrder.setOrderID(myOrder.getOrderID());
        when(myOrderRepository.findById(updatedOrder.getOrderID())).thenReturn(Optional.of(myOrder));
        when(myOrderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        Order result = myOrderService.updateOrder(updatedOrder.getOrderID(), updatedOrder);

        // Assert
        assertEquals(newUser.getUsername(), result.getMyUser().getUsername(),
                "The user in the updated order should have the new username");
    }

    /**
     * Tests that adding a null order throws an IllegalArgumentException.
     */
    @Test
    void whenAddNullOrder_thenIllegalArgumentExceptionIsThrown() {
        // Arrange
        final Order nullOrder = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> myOrderService.addOrder(nullOrder),
                "Adding a null order should throw IllegalArgumentException");
    }

    /**
     * Tests that updating an order with invalid data throws an ResponseStatusException.
     */
    @Test
    void whenUpdateOrderWithInvalidData_thenResponseStatusExceptionIsThrown() {
        // Arrange
        Order invalidOrder = new Order(LocalDateTime.now().plusDays(10), "InvalidStatus", BigDecimal.valueOf(-100), myTestUser);
        invalidOrder.setOrderID(1L);

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> myOrderService.updateOrder(invalidOrder.getOrderID(), invalidOrder),
                "Updating an order with invalid data should throw ResponseStatusException");
    }


    // Potentially add more tests for edge cases, but as of now there is full method coverage for OrderService

}
