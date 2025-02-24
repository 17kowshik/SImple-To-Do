package com.kowshik.ToDoApplication.service;

import com.kowshik.ToDoApplication.exception.TaskCreationException;
import com.kowshik.ToDoApplication.exception.TaskDeletionException;
import com.kowshik.ToDoApplication.exception.TaskNotFoundException;
import com.kowshik.ToDoApplication.exception.TaskUpdateException;
import com.kowshik.ToDoApplication.model.Task;
import com.kowshik.ToDoApplication.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        List<Task> taskList = taskRepository.findAll();
        if (taskList.isEmpty()) {
            throw new TaskNotFoundException("No tasks found. Start by creating a new one!");
        }
        return taskList;
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    public Task createTask(Task task) {
        if (isNotaValidTask(task)) {
            throw new TaskCreationException("Invalid task details");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(int id, Task task) {
        if (isNotaValidTask(task)) {
            throw new TaskUpdateException("Invalid task details");
        }
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskUpdateException("Task with ID " + id + " not found"));
        task.setId(existingTask.getId());

        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        if (!taskRepository.existsById(id)){
            throw new TaskDeletionException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private boolean isNotaValidTask(Task task){
        return task.getName() == null || task.getName().isBlank();
    }
}
