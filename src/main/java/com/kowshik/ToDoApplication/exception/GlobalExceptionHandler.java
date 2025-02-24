package com.kowshik.ToDoApplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> generateErrorResponse(String message, Exception e) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("error_id", UUID.randomUUID().toString());
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        errorResponse.put("exception", e.getClass().getSimpleName());
        errorResponse.put("error", message);
        return errorResponse;
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TaskCreationException.class, TaskUpdateException.class, TaskDeletionException.class})
    public ResponseEntity<Map<String, Object>> handleTaskOperationException(RuntimeException e) {
        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return new ResponseEntity<>("Something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
