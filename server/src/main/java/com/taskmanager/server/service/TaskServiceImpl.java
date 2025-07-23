package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repo;

    public TaskServiceImpl(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public Task createTask(Task task) {
        return repo.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    @Override
    public Task updateTask(Long id, Task task) {
        task.setId(id);
        return repo.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        repo.deleteById(id);
    }

    @Override
    public void deleteAllTasks() {
        repo.deleteAll();
    }
}
