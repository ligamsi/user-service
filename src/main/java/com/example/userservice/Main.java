package com.example.userservice;

import java.util.Scanner;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.example.userservice.exception.DuplicateEmailException;
import com.example.userservice.exception.DatabaseException;
import com.example.userservice.config.HibernateUtil;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            try (
                    Scanner scanner = new Scanner(System.in);
                    UserService userService = new UserService()
            ) {
                boolean running = true;
                while (running) {

                    System.out.println("\n===== USER SERVICE =====");
                    System.out.println("1. Create user");
                    System.out.println("2. Find user by ID");
                    System.out.println("3. Show all users");
                    System.out.println("4. Update user");
                    System.out.println("5. Delete user");
                    System.out.println("0. Exit");

                    System.out.print("Choose an option: ");

                    String choice = scanner.nextLine().trim();

                    switch (choice) {
                        case "1" -> createUser(scanner, userService);
                        case "2" -> findUser(scanner, userService);
                        case "3" -> showAllUsers(userService);
                        case "4" -> updateUser(scanner, userService);
                        case "5" -> deleteUser(scanner, userService);
                        case "0" -> {
                            running = false;
                            System.out.println("You are out");
                        }
                        default -> System.out.println("Invalid option. Try again.");
                    }
                }
            }

        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void createUser(
            Scanner scanner,
            UserService userService
    ) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        String ageInput = scanner.nextLine();

        try {
            Integer age = Integer.parseInt(ageInput);

            User user = new User(name, email, age);

            userService.createUser(user);

            System.out.println("User created successfully!");
            System.out.println(user);

        } catch (NumberFormatException exception) {
            System.out.println("Error: Age must be a number.");

        } catch (DuplicateEmailException exception) {
            System.out.println("Error: " + exception.getMessage());

        } catch (DatabaseException exception) {
            System.out.println("Database error: " + exception.getMessage());

        } catch (IllegalArgumentException exception) {
            System.out.println("Validation error: " + exception.getMessage());
        }
    }

    private static void findUser(
            Scanner scanner,
            UserService userService
    ) {
        System.out.print("Enter user ID: ");
        String idInput = scanner.nextLine();

        try {
            Long id = Long.parseLong(idInput);

            User user = userService.getUserById(id);

            System.out.println("User found successfully!");
            System.out.println(user);

        } catch (NumberFormatException exception) {
            System.out.println("Error: ID must be a number.");

        } catch (DatabaseException exception) {
            System.out.println("Database error: " + exception.getMessage());

        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void showAllUsers(UserService userService) {

        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            System.out.println("\n===== USERS =====");

            for (User user : users) {
                System.out.println(user);
            }

            System.out.println("Total users: " + users.size());

        } catch (DatabaseException exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    private static void updateUser(
            Scanner scanner,
            UserService userService
    ) {
        try {
            System.out.print("Enter user ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Enter new name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            System.out.print("Enter new age: ");
            Integer age = Integer.parseInt(scanner.nextLine());

            userService.updateUser(id, name, email, age);

            System.out.println("User updated successfully!");

        } catch (NumberFormatException exception) {
            System.out.println("Error: ID and age must be numbers.");

        } catch (DuplicateEmailException exception) {
            System.out.println("Error: " + exception.getMessage());

        } catch (DatabaseException exception) {
            System.out.println("Database error: " + exception.getMessage());

        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    private static void deleteUser(
            Scanner scanner,
            UserService userService
    ) {
        try {
            System.out.print("Enter user ID: ");
            Long id = Long.parseLong(scanner.nextLine());

            userService.deleteUser(id);

            System.out.println("User deleted successfully!");

        } catch (NumberFormatException exception) {
            System.out.println("Error: ID must be a number.");

        } catch (DatabaseException exception) {
            System.out.println("Database error: " + exception.getMessage());

        } catch (IllegalArgumentException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }
}