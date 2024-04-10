package org.example.cloudstorage.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("The user could not be found.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
