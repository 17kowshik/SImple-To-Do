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
    private String exception;
    private String error;
}
