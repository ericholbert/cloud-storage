package org.example.cloudstorage.exception;

import java.time.LocalDateTime;

public record ErrorTemplate(String title, int status, String detail, String instance, LocalDateTime timestamp) {
}
