package com.kowshik.ToDoApplication.controller;

import com.kowshik.ToDoApplication.model.Task;
import com.kowshik.ToDoApplication.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        List<Task> taskList = taskService.getAllTasks();
        if (!taskList.isEmpty()) {
            return new ResponseEntity<>(taskList, HttpStatus.OK);
        }
        return new ResponseEntity<>("No tasks found. Start by creating a new one!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable int id) {
        Task foundTask = taskService.getTaskById(id);
        if (foundTask != null){
            return new ResponseEntity<>(foundTask, HttpStatus.OK);
        }
        return new ResponseEntity<>("No task found with Given Id", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        if (createdTask != null) {
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create task. Please try again.", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not found or update failed.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        if (taskService.deleteTask(id)) {
            return new ResponseEntity<>("Task deleted successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Task not found.", HttpStatus.NOT_FOUND);
    }
}
