package com.example.userservice;

import com.example.userservice.config.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();

            System.out.println("Hibernate connected successfully!");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}