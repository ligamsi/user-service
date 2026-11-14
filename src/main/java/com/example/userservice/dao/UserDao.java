package com.example.userservice.dao;

import com.example.userservice.config.HibernateUtil;
import com.example.userservice.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

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

    public User findById(Long id) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.get(User.class, id);
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                    "FROM User",
                    User.class
            ).list();
        }
    }

    public boolean update(Long id, String name, String email, Integer age) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user == null) {
                transaction.commit();
                return false;
            }

            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            transaction.commit();
            return true;

        } catch (RuntimeException exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public boolean delete(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user == null) {
                transaction.commit();
                return false;
            }

            session.remove(user);

            transaction.commit();
            return true;

        } catch (RuntimeException exception) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }
}