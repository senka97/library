package com.hybrid.libraryproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler{

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException exception) {
        log.error("Access denied", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> authenticationException(InternalAuthenticationServiceException exception) {
        log.error("Authentication exception", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> businessException(BusinessException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<Object> deleteBookException(DeleteException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        String message = messages.stream().map(Object::toString).collect(Collectors.joining(", "));
        log.error("Validation error happened", exception);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class) // for deleteById
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        log.error("Expected to have at least one row/element but zero rows/elements were returned", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error("Entity not found exception", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("Constraint violation exception", exception);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleException(RuntimeException exception) {
        log.error("Unexpected exception caught while processing request", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error, contact administrator");
    }
}
