package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task);
    Optional<Task> getTaskById(long id);
    List<Task> getAllTasks();
    Task updateTask(Task task);
    void deleteTask(long id);
    void deleteAllTasks();
}
