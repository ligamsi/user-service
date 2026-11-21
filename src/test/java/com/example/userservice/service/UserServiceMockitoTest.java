package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.AfterEach;
import com.example.userservice.entity.User;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserServiceMockitoTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @AfterEach
    void tearDown() {
        userService.close();
    }

    @Test
    void deleteUser_shouldCallDao_whenUserExists() {

        when(userDao.delete(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userDao).delete(1L);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist() {

        when(userDao.delete(1L)).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(1L)
        );

        verify(userDao).delete(1L);
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        User user = new User();
        user.setId(1L);

        when(userDao.findById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        verify(userDao).findById(1L);
    }

    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {
        when(userDao.findById(1L)).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(1L)
        );

        verify(userDao).findById(1L);
    }

    @Test
    void getAllUsers_shouldReturnUsers_whenUsersExist() {
        List<User> users = List.of(
                new User(),
                new User()
        );

        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao).findAll();
    }

    @Test
    void updateUser_shouldCallDao_whenUserExists() {
        when(userDao.update(
                1L,
                "Ivan",
                "ivan@example.com",
                25
        )).thenReturn(true);

        userService.updateUser(
                1L,
                "Ivan",
                "ivan@example.com",
                25
        );

        verify(userDao).update(
                1L,
                "Ivan",
                "ivan@example.com",
                25
        );
    }

    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist() {
        when(userDao.update(
                1L,
                "Ivan",
                "ivan@example.com",
                25
        )).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(
                        1L,
                        "Ivan",
                        "ivan@example.com",
                        25
                )
        );

        verify(userDao).update(
                1L,
                "Ivan",
                "ivan@example.com",
                25
        );
    }

    @Test
    void createUser_shouldCallDao_whenDataIsValid() {
        User user = new User(
                "Ivan",
                "ivan@example.com",
                25
        );

        userService.createUser(user);

        verify(userDao).create(user);
    }

    @Test
    void createUser_shouldNotCallDao_whenDataIsInvalid() {
        User user = new User(
                "",
                "invalid-email",
                -5
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        verify(userDao, never()).create(user);
    }
}
