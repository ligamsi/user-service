package com.example.userservice.service;

import com.example.userservice.dao.UserDao;
import com.example.userservice.entity.User;
import java.util.List;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.example.userservice.exception.DuplicateEmailException;
import org.hibernate.exception.ConstraintViolationException;


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

            if (isDuplicateEmail(exception)) {
                throw new DuplicateEmailException(
                        "Email is already in use: " + user.getEmail(),
                        exception
                );
            }

            throw exception;
        }
    }

    public User getUserById(Long id) {
        User user = userDao.findById(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User with ID " + id + " not found"
            );
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id, String name, String email, Integer age) {
        validateUserData(name, email, age);

        try {
            boolean updated = userDao.update(id, name, email, age);

            if (!updated) {
                throw new IllegalArgumentException(
                        "User with ID " + id + " not found"
                );
            }

        } catch (RuntimeException exception) {

            if (isDuplicateEmail(exception)) {
                throw new DuplicateEmailException(
                        "Email is already in use: " + email,
                        exception
                );
            }

            throw exception;
        }
    }

    public void deleteUser(Long id) {
        boolean deleted = userDao.delete(id);

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

    @Override
    public void close() {
        validatorFactory.close();
    }
}