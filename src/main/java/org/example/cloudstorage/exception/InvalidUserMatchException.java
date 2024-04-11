package org.example.cloudstorage.exception;

public class InvalidUserMatchException extends RuntimeException {
    public InvalidUserMatchException() {
        super("No match is permitted between the users.");
    }

    public InvalidUserMatchException(String message) {
        super(message);
    }
}
