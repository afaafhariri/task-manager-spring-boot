package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(User user, Task task);
    Optional<Task> getTaskById(Long id, User user);
    List<Task> getAllTasks(User user);
    Task updateTask(Long id, Task task, User user);
    void deleteTask(Long id, User user);
    void deleteAllTasks(User user);
}