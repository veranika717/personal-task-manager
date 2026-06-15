package org.example.task_manager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleTaskNotFound(TaskNotFoundException ex) {
        log.warn("Task not found: {}", ex.getMessage());
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", "not found",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleTaskNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", "not found",
                "message", ex.getMessage()
        );
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 500,
                "error", "server error",
                "message", ex.getMessage()
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNoResource(NoResourceFoundException ex) {
        log.debug("Static resource not found: {}", ex.getMessage());
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", "not found",
                "message", ex.getMessage()
        );
    }
}