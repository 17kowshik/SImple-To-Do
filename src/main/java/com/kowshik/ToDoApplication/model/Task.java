package com.kowshik.ToDoApplication.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private CompletionStatus status;

    @Enumerated(EnumType.STRING)
    private PriorityLevel priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm")
    @DateTimeFormat(pattern = "ddMMyyyy HH:mm")
    private LocalDateTime dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy HH:mm")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CompletionStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    public enum PriorityLevel {
        LOW,
        MEDIUM,
        HIGH
    }
}