package org.example.cloudstorage.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.cloudstorage.exception.ErrorTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
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

    @AfterReturning(pointcut = "forControllerPackage()", returning = "returnedValue")
    public <T> void logAfterReturningControllerPackage(JoinPoint joinPoint, Object returnedValue) {
        String user = getCurrentUser(joinPoint);
        ResponseEntity<T> responseEntity = (ResponseEntity<T>) returnedValue;
        logger.info("[%s] Responded with status '%s'".formatted(user, responseEntity.getStatusCode()));
    }

    @AfterReturning(pointcut = "execution(* org.example.cloudstorage.exception.CloudStorageExceptionHandler.*(..))", returning = "returnedValue")
    public <T> void logAfterCloudStorageExceptionHandler(JoinPoint joinPoint, Object returnedValue) {
        ResponseEntity<T> responseEntity = (ResponseEntity<T>) returnedValue;
        ErrorTemplate errorTemplate = (ErrorTemplate) responseEntity.getBody();
        String user = "public user";
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof WebRequest request) {
                Principal principal = ((ServletWebRequest) request).getRequest().getUserPrincipal();
                if (principal != null) {
                    user = principal.getName();
                }
            }
        }
        String exception = errorTemplate.title();
        String exMessage = errorTemplate.detail();
        logger.warning("[%s] Caught exception '%s: %s'".formatted(user, exception, exMessage));
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
