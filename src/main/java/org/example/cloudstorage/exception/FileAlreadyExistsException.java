package org.example.cloudstorage.exception;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException() {
        super("Could not save the file that already exists.");
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
