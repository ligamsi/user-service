package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            List<User> users = userDao.findAll();

            if (users.isEmpty()) {
                System.out.println("No users found");
            } else {
                System.out.println("All users:");

                for (User user : users) {
                    System.out.println(user);
                }
            }

        } finally {
            HibernateUtil.shutdown();
        }
    }
}