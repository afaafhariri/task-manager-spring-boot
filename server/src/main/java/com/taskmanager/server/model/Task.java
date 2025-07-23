package com.taskmanager.server.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Boolean completed = false;
}
