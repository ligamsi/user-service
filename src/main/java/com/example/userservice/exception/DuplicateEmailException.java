package com.example.userservice.exception;

public class DuplicateEmailException extends RuntimeException {

  public DuplicateEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}