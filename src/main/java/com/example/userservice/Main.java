package com.example.userservice;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserDao userDao = new UserDao();

            userDao.delete(1L);

            User user = userDao.findById(1L);

            if (user == null) {
                System.out.println("User successfully deleted");
            } else {
                System.out.println("User still exists:");
                System.out.println(user);
            }

        } finally {
            HibernateUtil.shutdown();
        }
    }
}