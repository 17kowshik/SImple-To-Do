package com.kowshik.ToDoApplication.exception;

public class TaskDeletionException extends RuntimeException {
    public TaskDeletionException(String message) {
        super(message);
    }
}
