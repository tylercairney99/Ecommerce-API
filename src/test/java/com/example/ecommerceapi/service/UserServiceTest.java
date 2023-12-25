package com.example.ecommerceapi.service;

import com.example.ecommerceapi.api.model.Product;
import com.example.ecommerceapi.api.model.User;
import com.example.ecommerceapi.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The UserServiceTest class contains tests for the UserService class.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    /**
     * A mock version of UserRepository for use in our tests without needing a real database.
     */
    @Mock
    private UserRepository myUserRepository;

    /**
     * The UserService instance with the injected mock UserRepository.
     */
    @InjectMocks
    private UserService myUserService;

    /**
     * A sample user used for test cases.
     */
    private User myUser;

    /**
     * Sets up a dummy user for testing before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize a dummy user for use in tests
        myUser = new User();
        myUser.setUserID(1L); // Assuming there's a setter for the ID in your User class
        // Setup other necessary properties of the User
    }

    /**
     * Test case to verify that when adding a user, the saved user's fields match the expected values.
     */
    @Test
    void whenAddUser_thenSaveUser() {
        // Arrange
        when(myUserRepository.save(any(User.class))).thenReturn(myUser);

        // Act
        User savedUser = myUserService.addUser(myUser);

        // Assert
        assertNotNull(savedUser);
        assertEquals(myUser.getUserID(), savedUser.getUserID());
        verify(myUserRepository).save(myUser);
    }

    /**
     * Test case to verify that when retrieving all users, a non-empty user list is returned.
     */
    @Test
    void whenGetAllUsers_thenUserList() {
        // Arrange
        when(myUserRepository.findAll()).thenReturn(Arrays.asList(myUser));

        // Act
        List<User> users = myUserService.getAllUsers();

        // Assert
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(myUserRepository).findAll();
    }

    /**
     * Test case to verify that when retrieving a user by ID, an Optional containing the user is returned.
     */
    @Test
    void whenGetUserByID_thenUserOptional() {
        // Arrange
        when(myUserRepository.findById(myUser.getUserID())).thenReturn(Optional.of(myUser));

        // Act
        Optional<User> foundUser = myUserService.getUserByID(myUser.getUserID());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(myUser.getUserID(), foundUser.get().getUserID());
        verify(myUserRepository).findById(myUser.getUserID());
    }

    /**
     * Test case to verify that when updating a user, all fields are correctly updated to the new values.
     */
    @Test
    void whenUpdateUser_thenUpdatedUser() {
        // Arrange
        User validUpdatedUser = new User("UpdatedUsername", "UpdatedPassword", "updated@example.com");
        validUpdatedUser.setUserID(myUser.getUserID());

        when(myUserRepository.findById(validUpdatedUser.getUserID())).thenReturn(Optional.of(myUser));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        myUserService.updateUser(validUpdatedUser.getUserID(), validUpdatedUser);

        // Assert
        verify(myUserRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertNotNull(capturedUser, "Updated user should not be null");
        assertEquals(validUpdatedUser.getUsername(), capturedUser.getUsername(), "Username should match the updated username");
        assertEquals(validUpdatedUser.getPassword(), capturedUser.getPassword(), "Password should match the updated password");
        assertEquals(validUpdatedUser.getEmail(), capturedUser.getEmail(), "Email should match the updated email");
    }

    /**
     * Test case to verify that when deleting a user, the user is successfully deleted.
     */
    @Test
    void whenDeleteUser_thenUserDeleted() {
        // Arrange
        when(myUserRepository.findById(myUser.getUserID())).thenReturn(Optional.of(myUser));
        doNothing().when(myUserRepository).delete(myUser);

        // Act & Assert
        assertDoesNotThrow(() -> myUserService.deleteUser(myUser.getUserID()));
        verify(myUserRepository).delete(myUser);
    }

    /**
     * Test case to verify that when attempting to update a non-existent user, a ResponseStatusException is thrown.
     */
    @Test
    void whenUpdateNonExistentUser_thenThrowException() {
        // Arrange
        User validUserDetails = new User("ValidUsername", "ValidPassword", "valid@email.com");
        validUserDetails.setUserID(999L); // Non-existent user ID

        when(myUserRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class,
                () -> myUserService.updateUser(validUserDetails.getUserID(), validUserDetails));
        verify(myUserRepository, never()).save(any(User.class));
    }

    /**
     * Test case to verify that when adding a user with all fields specified,
     * the added user's fields match the expected values.
     */
    @Test
    void whenAddUserWithAllFields_thenAllFieldsAreCorrect() {
        when(myUserRepository.save(any(User.class))).thenReturn(myUser);
        User savedUser = myUserService.addUser(myUser);
        assertNotNull(savedUser, "Saved user should not be null");
        assertEquals(myUser.getUsername(), savedUser.getUsername(), "Usernames should match");
        assertEquals(myUser.getPassword(), savedUser.getPassword(), "Passwords should match");
        assertEquals(myUser.getEmail(), savedUser.getEmail(), "Emails should match");
        verify(myUserRepository).save(myUser);
    }

    /**
     * Test case to verify that when updating a user, all fields are correctly updated
     * to the new values.
     */
    @Test
    void whenUpdateUser_thenAllFieldsAreUpdated() {
        User updatedInfo = new User("UpdatedUser", "UpdatedPassword", "updated@example.com");
        updatedInfo.setUserID(myUser.getUserID());
        when(myUserRepository.findById(myUser.getUserID())).thenReturn(Optional.of(myUser));
        when(myUserRepository.save(any(User.class))).thenReturn(updatedInfo);
        User result = myUserService.updateUser(updatedInfo.getUserID(), updatedInfo);
        assertNotNull(result, "Updated user should not be null");
        assertEquals(updatedInfo.getUsername(), result.getUsername(), "Usernames should be updated");
        assertEquals(updatedInfo.getPassword(), result.getPassword(), "Passwords should be updated");
        assertEquals(updatedInfo.getEmail(), result.getEmail(), "Emails should be updated");
    }

    /**
     * Test case to verify that attempting to add a null user results in an
     * IllegalArgumentException being thrown.
     */
    @Test
    void whenAddNullUser_thenIllegalArgumentExceptionIsThrown() {
        final User nullUser = null;
        assertThrows(IllegalArgumentException.class, () -> myUserService.addUser(nullUser),
                "Adding a null user should throw IllegalArgumentException");
    }

    /**
     * Test case to verify that attempting to update a user with invalid data
     * results in an IllegalArgumentException being thrown.
     */
    @Test
    void whenUpdateUserWithInvalidData_thenIllegalArgumentExceptionIsThrown() {
        User invalidUser = new User("", "", "");
        invalidUser.setUserID(myUser.getUserID());
        assertThrows(IllegalArgumentException.class, () -> myUserService.updateUser(invalidUser.getUserID(), invalidUser),
                "Updating a user with invalid data should throw IllegalArgumentException");
    }
}
