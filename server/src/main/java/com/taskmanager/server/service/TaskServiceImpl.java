package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repo;

    public TaskServiceImpl(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public Task createTask(User user, Task task) {
        task.setUser(user);
        return repo.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long id, User user) {
        return repo.findByIdAndUser(id, user);
    }

    @Override
    public List<Task> getAllTasks(User user) {
        return repo.findByUser(user);
    }

    @Override
    public Task updateTask(Long id, Task task, User user) {
        return repo.findByIdAndUser(id, user)
                .map(existing -> {
                    existing.setTitle(task.getTitle());
                    existing.setDescription(task.getDescription());
                    existing.setCompleted(task.getCompleted());
                    return repo.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    @Override
    public void deleteTask(Long id, User user) {
        repo.findByIdAndUser(id, user)
                .ifPresent(task -> repo.delete(task));
    }

    @Override
    @Transactional
    public void deleteAllTasks(User user) {
        repo.deleteByUser(user);
    }
}