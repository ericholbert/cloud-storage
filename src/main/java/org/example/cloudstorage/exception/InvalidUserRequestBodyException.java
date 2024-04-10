package org.example.cloudstorage.exception;

public class InvalidUserRequestBodyException extends RuntimeException {
    public InvalidUserRequestBodyException() {
        super("The request body is missing necessary JSON properties.");
    }

    public InvalidUserRequestBodyException(String message) {
        super(message);
    }
}
