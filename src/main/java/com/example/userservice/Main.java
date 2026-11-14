package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            User user = new User(
                    "Ivan",
                    "ivan@gmail.com",
                    25
            );

            userDao.create(user);

            System.out.println("User created successfully!");
            System.out.println(user);

        } finally {
            HibernateUtil.shutdown();
        }
    }
}