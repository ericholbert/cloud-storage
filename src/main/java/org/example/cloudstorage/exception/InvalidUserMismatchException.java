package org.example.cloudstorage.exception;

public class InvalidUserMismatchException extends  RuntimeException {
    public InvalidUserMismatchException() {
        super("No match found between the users.");
    }

    public InvalidUserMismatchException(String message) {
        super(message);
    }
}
