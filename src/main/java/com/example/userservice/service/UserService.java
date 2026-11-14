package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;

public class UserService {

    private final UserDao userDao = new UserDao();

    public void createUser(User user) {
        validateUserData(
                user.getName(),
                user.getEmail(),
                user.getAge()
        );

        userDao.create(user);
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUserData(name, email, age);

        userDao.update(id, name, email, age);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    private void validateUserData(String name, String email, Integer age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (age != null && age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}