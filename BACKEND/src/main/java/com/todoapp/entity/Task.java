package com.todoapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_deadline", columnList = "deadline"),
    @Index(name = "idx_priority", columnList = "priority"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_description", nullable = false, length = 500)
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Priority {
        URGENT, NORMAL, LOW
    }

    public enum Status {
        NOT_STARTED, IN_PROGRESS, DONE, MISSED_DEADLINE
    }
}
