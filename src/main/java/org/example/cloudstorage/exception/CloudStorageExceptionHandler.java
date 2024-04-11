package org.example.cloudstorage.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class CloudStorageExceptionHandler extends ResponseEntityExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @ExceptionHandler(value = {FileNotWrittenException.class, FileNotReadException.class})
    ResponseEntity<ErrorTemplate> handleInternalException(RuntimeException e, WebRequest request) {
        return getResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    ResponseEntity<ErrorTemplate> handleNotAuthorizeException(RuntimeException e, WebRequest request) {
        return getResponseEntity(e, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {FileNotFoundException.class, UserNotFoundException.class})
    ResponseEntity<ErrorTemplate> handleNotFoundException(RuntimeException e, WebRequest request) {
        return getResponseEntity(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {InvalidSortParameterException.class, InvalidUserMatchException.class, InvalidUserMismatchException.class, FileAlreadyExistsException.class, InvalidUserRequestBodyException.class})
    ResponseEntity<ErrorTemplate> handleBadRequestException(RuntimeException e, WebRequest request) {
        return getResponseEntity(e, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(ex, status, request, "The uploaded file must not exceeds %s.".formatted(maxFileSize));
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return getResponseEntity(ex, status, request, "The requested page does not exists.");
    }

    private <T, S extends Exception, U extends HttpStatusCode> ResponseEntity<T> getResponseEntity(S e, U status, WebRequest request) {
        return getResponseEntity(e, status, request, e.getMessage());
    }

    private <T, S extends Exception, U extends HttpStatusCode> ResponseEntity<T> getResponseEntity(S e, U status, WebRequest request, String detail) {
        T error = (T) new ErrorTemplate(e.getClass().getSimpleName(), status.value(), detail, request.getContextPath(), LocalDateTime.now());
        return new ResponseEntity<T>(error, status);
    }
}
