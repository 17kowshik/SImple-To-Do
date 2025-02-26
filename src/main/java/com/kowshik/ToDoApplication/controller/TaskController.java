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
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(id, task), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/tasks/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        return new ResponseEntity<>(taskService.searchTasks(keyword), HttpStatus.OK);
    }

    @GetMapping("/tasks/getTasksDueInNextNDays")
    public ResponseEntity<List<Task>> getTasksDueInNextNDays(@RequestParam int days) {
        return new ResponseEntity<>(taskService.getTasksDueInNextNDays(days), HttpStatus.OK);
    }

    @GetMapping("/tasks/filterByPriority")
    public ResponseEntity<List<Task>> filterByPriority(@RequestParam Task.PriorityLevel priority) {
        return new ResponseEntity<>(taskService.filterByPriority(priority), HttpStatus.OK);
    }

    @GetMapping("/tasks/filterByStatus")
    public ResponseEntity<List<Task>> filterByStatus(@RequestParam Task.CompletionStatus status) {
        return new ResponseEntity<>(taskService.filterByStatus(status), HttpStatus.OK);
    }
}