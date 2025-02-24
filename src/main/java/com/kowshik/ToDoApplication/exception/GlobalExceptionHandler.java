package com.kowshik.ToDoApplication.exception;

import com.kowshik.ToDoApplication.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse generateErrorResponse(String message, Exception e) {
        return new ErrorResponse(
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                e.getClass().getSimpleName(),
                message
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TaskCreationException.class, TaskUpdateException.class, TaskDeletionException.class})
    public ResponseEntity<ErrorResponse> handleTaskOperationException(RuntimeException e) {
        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(generateErrorResponse(e.getMessage(), e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
