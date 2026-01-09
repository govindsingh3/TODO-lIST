package com.todoapp.dto;

import com.todoapp.entity.Task;
import com.todoapp.entity.Task.Priority;
import com.todoapp.entity.Task.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;
    private String taskDescription;
    private Priority priority;
    private Status status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private Long userId;

    public static TaskDTO fromEntity(Task task) {
        return TaskDTO.builder()
            .id(task.getId())
            .taskDescription(task.getTaskDescription())
            .priority(task.getPriority())
            .status(task.getStatus())
            .deadline(task.getDeadline())
            .createdAt(task.getCreatedAt())
            .userId(task.getUser().getId())
            .build();
    }
}
