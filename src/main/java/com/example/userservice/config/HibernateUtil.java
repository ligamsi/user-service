package com.example.userservice.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .configure();

            configuration.setProperty(
                    "hibernate.connection.url",
                    getRequiredEnv("DB_URL")
            );

            configuration.setProperty(
                    "hibernate.connection.username",
                    getRequiredEnv("DB_USERNAME")
            );

            configuration.setProperty(
                    "hibernate.connection.password",
                    getRequiredEnv("DB_PASSWORD")
            );

            return configuration.buildSessionFactory();

        } catch (Throwable exception) {
            exception.printStackTrace();
            throw new ExceptionInInitializerError(exception);
        }
    }

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable " + name + " is not set"
            );
        }

        return value;
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}