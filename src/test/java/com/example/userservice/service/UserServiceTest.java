package com.example.userservice.service;

import com.example.userservice.entity.User;
import org.junit.jupiter.api.Test;
import com.example.userservice.exception.DuplicateEmailException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void createUser_shouldThrowException_whenNameIsBlank() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "",
                    "test@example.com",
                    25
            );
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(user)
            );
        }
    }

    @Test
    void createUser_shouldThrowException_whenEmailIsBlank() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "Test User",
                    "",
                    25
            );
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(user)
            );
        }
    }

    @Test
    void createUser_shouldThrowException_whenEmailIsInvalid() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "Test User",
                    "invalid-email",
                    25
            );
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(user)
            );
        }
    }

    @Test
    void createUser_shouldThrowException_whenAgeIsNegative() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "Test User",
                    "test@example.com",
                    -1
            );
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(user)
            );
        }
    }

    @Test
    void createUser_shouldNotThrowException_whenDataIsValid() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "Test User6",
                    "valid6@example.com",
                    25
            );
            assertDoesNotThrow(
                    () -> userService.createUser(user)
            );
            assertNotNull(user.getId());

            User savedUser = userService.getUserById(user.getId());

            assertNotNull(savedUser);
            assertEquals("Test User6", savedUser.getName());
            assertEquals("valid6@example.com", savedUser.getEmail());
            assertEquals(25, savedUser.getAge());

            userService.updateUser(
                    user.getId(),
                    "Updated User2",
                    "updated2@example.com",
                    30
            );

            User updatedUser = userService.getUserById(user.getId());

            assertEquals("Updated User2", updatedUser.getName());
            assertEquals("updated2@example.com", updatedUser.getEmail());
            assertEquals(30, updatedUser.getAge());

            userService.deleteUser(user.getId());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.getUserById(user.getId())
            );
        }
    }

    @Test
    void getAllUsers_shouldReturnUsers_whenUsersExist() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "List Test User",
                    "list-test@example.com",
                    28
            );
            userService.createUser(user);

            var users = userService.getAllUsers();

            assertNotNull(users);
            assertTrue(
                    users.stream().anyMatch(
                            foundUser -> foundUser.getId().equals(user.getId())
                    )
            );

            userService.deleteUser(user.getId());
        }
    }

    @Test
    void createUser_shouldThrowException_whenEmailAlreadyExists() {
        try (UserService userService = new UserService()) {
            User user = new User(
                    "Another User",
                    "valid@example.com",
                    30
            );
            assertThrows(
                    DuplicateEmailException.class,
                    () -> userService.createUser(user)
            );
        }
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        try (UserService userService = new UserService()) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.getUserById(-1L)
            );
        }
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {
        try (UserService userService = new UserService()) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.deleteUser(-1L)
            );
        }
    }

    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist() {
        try (UserService userService = new UserService()) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.updateUser(
                            -1L,
                            "Updated User",
                            "updated-test@example.com",
                            30
                    )
            );
        }
    }
}