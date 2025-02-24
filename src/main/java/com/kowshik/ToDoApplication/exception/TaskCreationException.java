package com.kowshik.ToDoApplication.exception;

public class TaskCreationException extends RuntimeException{
    public TaskCreationException(String message) {
        super(message);
    }
}
