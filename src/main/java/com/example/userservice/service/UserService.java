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


public class UserService implements AutoCloseable {

    private final UserDao userDao = new UserDao();
    private final ValidatorFactory validatorFactory =
            Validation.buildDefaultValidatorFactory();

    private final Validator validator =
            validatorFactory.getValidator();

    public void createUser(User user) {
        validateUserData(
                user.getName(),
                user.getEmail(),
                user.getAge()
        );

        try {
            userDao.create(user);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(
                    exception,
                    user.getEmail()
            );
        }
    }

    public User getUserById(Long id) {
        User user;

        try {
            user = userDao.findById(id);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }

        if (user == null) {
            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }

        return user;
    }

    public List<User> getAllUsers() {
        try {
            return userDao.findAll();

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUserData(name, email, age);

        boolean updated;

        try {
            updated = userDao.update(id, name, email, age);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, email);
        }

        if (!updated) {
            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }
    }

    public void deleteUser(Long id) {
        boolean deleted;

        try {
            deleted = userDao.delete(id);

        } catch (RuntimeException exception) {
            throw handleDatabaseException(exception, null);
        }

        if (!deleted) {
            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }
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
            return new DuplicateEmailException(
                    "Email is already in use: " + email,
                    exception
            );
        }

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