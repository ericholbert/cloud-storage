package org.example.cloudstorage.exception;

public class FileNotReadException extends RuntimeException {
    public FileNotReadException() {
        super("Could not read the file from the disk.");
    }

    public FileNotReadException(String message) {
        super(message);
    }
}
