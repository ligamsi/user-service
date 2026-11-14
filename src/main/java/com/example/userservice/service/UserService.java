package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;

public class UserService {

    private final UserDao userDao = new UserDao();

    public void createUser(User user) {
        userDao.create(user);
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        userDao.update(id, name, email, age);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}