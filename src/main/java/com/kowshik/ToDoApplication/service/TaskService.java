package com.kowshik.ToDoApplication.service;

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
        return taskRepository.findAll();
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        return validateTask(task) ? taskRepository.save(task) : null;
    }

    public Task updateTask(int id, Task task) {
        if (taskRepository.existsById(id) && validateTask(task)) {
            return taskRepository.save(task);
        }
        return null;
    }

    public boolean deleteTask(int id) {
        if (taskRepository.existsById(id)){
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private boolean validateTask(Task task){
        return task.getName() != null && !task.getName().isBlank();
    }
}
