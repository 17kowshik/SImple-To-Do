package com.kowshik.ToDoApplication.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private String errorId;
    private String timestamp;
    private String error;
    private String exception;
    private int statusCode;
    private String status;
    private String path;
}
