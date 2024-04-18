package org.example.cloudstorage.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class CloudStorageLogging {
    private Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("within(org.example.cloudstorage.controller..*)")
    private void forControllerPackage() {}

    @Before("forControllerPackage()")
    public void logBeforeControllerPackage(JoinPoint joinPoint) {
        String user = getCurrentUser(joinPoint);
        String method = joinPoint.getSignature().toShortString();
        logger.info("[%s] Requested '%s'".formatted(user, method));
    }

    // TODO: None-custom exceptions managed by ExceptionHandlerExceptionResolver cannot be caught here for custom logging
    @AfterThrowing(pointcut = "forControllerPackage()", throwing = "e")
    public void logAfterThrowingControllerPackage(JoinPoint joinPoint, Throwable e) {
        String user = getCurrentUser(joinPoint);
        String exception = e.getClass().getName();
        String exMessage = e.getMessage();
        logger.warning("[%s] Caught exception '%s: %s'".formatted(user, exception, exMessage));
    }

    @AfterReturning(pointcut = "forControllerPackage()", returning = "returnedValue")
    public <T> void logAfterReturningControllerPackage(JoinPoint joinPoint, Object returnedValue) {
        String user = getCurrentUser(joinPoint);
        ResponseEntity<T> responseEntity = (ResponseEntity<T>) returnedValue;
        logger.info("[%s] Responded with status '%s'".formatted(user, responseEntity.getStatusCode()));
    }

    @Before("within(org.example.cloudstorage.service..*)")
    public void logBeforeServicePackage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String method = joinPoint.getSignature().toShortString();
        for (Object arg : args) {
            if (arg instanceof MultipartFile mpFile) {
                arg = "MultipartFile(name=%s, size=%d, type=%s, [...])".formatted(mpFile.getOriginalFilename(), mpFile.getSize(), mpFile.getContentType());
            }
            logger.log(Level.FINE, "[%s] Called with arg '%s'".formatted(method, arg));
        }
    }

    private String getCurrentUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String user = "public user";
        for (Object arg : args) {
            if (arg instanceof Principal principal) {
                user = principal.getName();
            }
        }
        return user;
    }
}
