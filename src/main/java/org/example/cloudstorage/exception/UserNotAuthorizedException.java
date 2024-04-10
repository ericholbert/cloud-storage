package org.example.cloudstorage.exception;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("You are not authorized to perform this action.");
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
