package com.kowshik.ToDoApplication.exception;

import com.kowshik.ToDoApplication.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static void logErrors(String errorId, Exception e, HttpStatus status, String path) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];

        logger.error("Error ID: {} - {}: {} (Status: {} {} | Path: {} | Location: {}.{}:{})",
                errorId,
                e.getClass().getSimpleName(),
                e.getMessage(),
                status.value(), status.name(),
                path,
                stackTraceElement.getClassName(),
                stackTraceElement.getMethodName(),
                stackTraceElement.getLineNumber());
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        String errorId = UUID.randomUUID().toString();
        String path = request.getRequestURI();

        logErrors(errorId, e, status, path);

        ErrorResponse errorResponse = new ErrorResponse(
                errorId,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                e.getMessage(),
                e.getClass().getSimpleName(),
                status.value(),
                status.name(),
                path
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException e, HttpServletRequest request) {
        return generateErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({TaskCreationException.class, TaskUpdateException.class, TaskDeletionException.class, WeakPasswordException.class})
    public ResponseEntity<ErrorResponse> handleTaskOperationException(RuntimeException e, HttpServletRequest request) {
        return generateErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({InvalidCredentialsException.class, UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(RuntimeException e, HttpServletRequest request) {
        return generateErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException e, HttpServletRequest request) {
        return generateErrorResponse(e, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        return generateErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}