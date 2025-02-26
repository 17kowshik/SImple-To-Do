package com.kowshik.ToDoApplication.service;

import com.kowshik.ToDoApplication.exception.TaskCreationException;
import com.kowshik.ToDoApplication.exception.TaskDeletionException;
import com.kowshik.ToDoApplication.exception.TaskNotFoundException;
import com.kowshik.ToDoApplication.exception.TaskUpdateException;
import com.kowshik.ToDoApplication.model.Task;
import com.kowshik.ToDoApplication.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        if (taskList.isEmpty()) {
            throw new TaskNotFoundException("No tasks found. Start by creating a new one!");
        }
        logger.info("Retrieved all tasks successfully. Total tasks: {}", taskList.size());
        return taskList;
    }

    public Task getTaskById(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        logger.info("Retrieved task with ID: {}", id);
        return task;
    }

    private String getTaskValidationError(Task task) {
        if (task.getName() == null || task.getName().isBlank()) {
            return "Task name cannot be empty.";
        }
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            return "Due date cannot be in the past.";
        }
        return null;
    }

    public Task createTask(Task task) {
        String validationError = getTaskValidationError(task);
        if (validationError != null) {
            throw new TaskCreationException(validationError);
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Created task with ID: {}", savedTask.getId());
        return savedTask;
    }

    public Task updateTask(int id, Task task) {
        String validationError = getTaskValidationError(task);
        if (validationError != null) {
            throw new TaskUpdateException(validationError);
        }
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskUpdateException("Task with ID " + id + " not found"));
        task.setId(existingTask.getId());
        Task updatedTask = taskRepository.save(task);
        logger.info("Updated task with ID: {}", updatedTask.getId());
        return updatedTask;
    }

    public List<Task> searchTasks(String keyword) {
        List<Task> taskList = taskRepository.searchTasks(keyword);
        if (taskList.isEmpty()){
            throw new TaskNotFoundException("No tasks found for the keyword: " + keyword);
        }
        logger.info("Retrieved tasks for the keyword: {}. Total tasks: {}", keyword, taskList.size());
        return taskList;
    }

    public void deleteTask(int id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskDeletionException("Task not found");
        }
        taskRepository.deleteById(id);
        logger.info("Deleted task with ID: {}", id);
    }

    public List<Task> getTasksDueInNextNDays(int days) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime futureDate = currentDate.plusDays(days);
        List<Task> tasks = taskRepository.filterByDueDate(currentDate, futureDate);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks due in the next " + days + " days.");
        }
        logger.info("Retrieved {} tasks due in the next {} days", tasks.size(), days);
        return tasks;
    }

    public List<Task> filterByPriority(Task.PriorityLevel priority) {
        List<Task> tasks = taskRepository.filterByPriority(priority);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found with priority: " + priority);
        }
        logger.info("Retrieved {} tasks with priority: {}", tasks.size(), priority);
        return tasks;
    }

    public List<Task> filterByStatus(Task.CompletionStatus status) {
        List<Task> tasks = taskRepository.filterByCompletionStatus(status);
        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found with status: " + status);
        }
        logger.info("Retrieved {} tasks with status: {}", tasks.size(), status);
        return tasks;
    }
}