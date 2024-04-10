package org.example.cloudstorage.exception;

public class FileNotWrittenException extends RuntimeException {
    public FileNotWrittenException() {
        super("Could not write the file to the disk.");
    }

    public FileNotWrittenException(String message) {
        super(message);
    }
}
