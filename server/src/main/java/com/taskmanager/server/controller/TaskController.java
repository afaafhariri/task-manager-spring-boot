package com.taskmanager.server.controller;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.User;
import com.taskmanager.server.payload.LoginRequest;
import com.taskmanager.server.repository.UserRepository;
import com.taskmanager.server.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepo;

    public TaskController(TaskService taskService, UserRepository userRepo) {
        this.taskService = taskService;
        this.userRepo    = userRepo;
    }

    private User getCurrentUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(Authentication auth,
                                           @RequestBody Task task) {
        User user = getCurrentUser(auth);
        Task created = taskService.createTask(user, task);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication auth) {
        User user = getCurrentUser(auth);
        return ResponseEntity.ok(taskService.getAllTasks(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(Authentication auth,
                                            @PathVariable Long id) {
        User user = getCurrentUser(auth);
        return taskService.getTaskById(id, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(Authentication auth,
                                           @PathVariable Long id,
                                           @RequestBody Task taskDetails) {
        User user = getCurrentUser(auth);
        try {
            Task updated = taskService.updateTask(id, taskDetails, user);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication auth,
                                           @PathVariable Long id) {
        User user = getCurrentUser(auth);
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllTasks(Authentication auth) {
        User user = getCurrentUser(auth);
        taskService.deleteAllTasks(user);
        return ResponseEntity.noContent().build();
    }
}