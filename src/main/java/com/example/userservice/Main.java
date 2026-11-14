package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            User user = userDao.findById(1L);

            if (user != null) {
                System.out.println("User found:");
                System.out.println(user);
            } else {
                System.out.println("User not found");
            }

        } finally {
            HibernateUtil.shutdown();
        }
    }
}