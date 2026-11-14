package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;

public class Main {

    public static void main(String[] args) {
        try {
            UserService userService = new UserService();

            User user = new User(
                    "Ivan",
                    "ivan.ivan@example.com",
                    25
            );

            userService.createUser(user);

            System.out.println("User created:");
            System.out.println(user);

        } finally {
            HibernateUtil.shutdown();
        }
    }
}