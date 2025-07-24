package com.taskmanager.server.controller;

import com.taskmanager.server.model.User;
import com.taskmanager.server.payload.LoginRequest;
import com.taskmanager.server.payload.RegisterRequest;
import com.taskmanager.server.payload.JwtResponse;
import com.taskmanager.server.repository.UserRepository;
import com.taskmanager.server.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepo,
                          PasswordEncoder passwordEncoder) {
        this.authManager     = authManager;
        this.jwtUtil         = jwtUtil;
        this.userRepo        = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // --- LOGIN endpoint ---
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtUtil.generateToken(auth);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    // --- REGISTER endpoint ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
        User newUser = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepo.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
}