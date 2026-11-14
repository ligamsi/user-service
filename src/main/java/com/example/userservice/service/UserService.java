package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.example.userservice.exception.DuplicateEmailException;
import org.hibernate.exception.ConstraintViolationException;
import com.example.userservice.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao = new UserDao();
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private final Validator validator =
            validatorFactory.getValidator();

    public void createUser(User user) {
        validateUserData(
                user.getName(),
                user.getEmail(),
                user.getAge()
        );

        logger.info("Creating user with email: {}", user.getEmail());

        try {
            userDao.create(user);
            logger.info("User created successfully with ID: {}", user.getId());

        } catch (RuntimeException exception) {
            throw handleDatabaseException(
                    exception,
                    user.getEmail()
            );
        }
    }

    public User getUserById(Long id) {
        logger.info("Finding user with ID: {}", id);
        User user;
        try {
            user = userDao.findById(id);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }
        if (user == null) {
            logger.warn("User with ID {} not found", id);

            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }
        logger.info("User with ID {} found successfully", id);
        return user;
    }

    public List<User> getAllUsers() {
        logger.info("Finding all users");
        try {
            List<User> users = userDao.findAll();
            logger.info("Found {} users", users.size());
            return users;

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUserData(name, email, age);
        logger.info("Updating user with ID: {}", id);
        boolean updated;
        try {
            updated = userDao.update(id, name, email, age);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, email);
        }
        if (!updated) {
            logger.warn("User with ID {} not found for update", id);

            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }

        logger.info("User with ID {} updated successfully", id);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        boolean deleted;
        try {
            deleted = userDao.delete(id);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }
        if (!deleted) {
            logger.warn("User with ID {} not found for deletion", id);

            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }
        logger.info("User with ID {} deleted successfully", id);
    }

    private void validateUserData(String name, String email, Integer age) {
        User user = new User(name, email, age);

        var violations = validator.validate(user);

        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();

            for (var violation : violations) {
                errors.append(violation.getMessage()).append("; ");
            }

            throw new IllegalArgumentException(errors.toString());
        }
    }

    private boolean isDuplicateEmail(RuntimeException exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintException) {
                return "23505".equals(
                        constraintException.getSQLException().getSQLState()
                );
            }
            cause = cause.getCause();
        }

        return false;
    }

    private RuntimeException handleDatabaseException(
            RuntimeException exception,
            String email
    ) {
        if (isDuplicateEmail(exception)) {
            logger.warn("Duplicate email detected: {}", email);

            return new DuplicateEmailException(
                    "Email is already in use: " + email,
                    exception
            );
        }
        logger.error("Database operation failed", exception);

        return new DatabaseException(
                "Database operation failed",
                exception
        );
    }

    @Override
    public void close() {
        validatorFactory.close();
    }
}