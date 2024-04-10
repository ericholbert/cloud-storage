package org.example.cloudstorage.exception;

public class InvalidSortParameterException extends RuntimeException {
    public InvalidSortParameterException() {
        super("You have provided an invalid sort parameter.");
    }

    public InvalidSortParameterException(String message) {
        super(message);
    }
}
