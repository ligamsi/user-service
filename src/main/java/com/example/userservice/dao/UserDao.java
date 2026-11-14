package com.example.userservice.dao;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {

    public void create(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

        } catch (RuntimeException exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }
}