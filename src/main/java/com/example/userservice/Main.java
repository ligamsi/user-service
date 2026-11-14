package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            userDao.update(1L, "Kolya", "kolya@example.com", 35);

            User user = userDao.findById(1L);

            if (user != null) {
                System.out.println("Updated user:");
                System.out.println(user);
            } else {
                System.out.println("User not found");
            }

        } finally {
            HibernateUtil.shutdown();
        }
    }
}