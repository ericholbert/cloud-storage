package org.example.cloudstorage.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("Could not find the file.");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
