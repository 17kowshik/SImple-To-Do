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

    public Task createTask(Task task) {
        if (isNotaValidTask(task)) {
            throw new TaskCreationException("Invalid task details");
        }
        Task savedTask = taskRepository.save(task);
        logger.info("Created task with ID: {}", savedTask.getId());
        return savedTask;
    }

    public Task updateTask(int id, Task task) {
        if (isNotaValidTask(task)) {
            throw new TaskUpdateException("Invalid task details");
        }
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskUpdateException("Task with ID " + id + " not found"));
        task.setId(existingTask.getId());
        Task updatedTask = taskRepository.save(task);
        logger.info("Updated task with ID: {}", updatedTask.getId());
        return updatedTask;
    }

    public void deleteTask(int id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskDeletionException("Task not found");
        }
        taskRepository.deleteById(id);
        logger.info("Deleted task with ID: {}", id);
    }

    private boolean isNotaValidTask(Task task) {
        return task.getName() == null || task.getName().isBlank();
    }
}