package com.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.todoapp.entity.Task.Priority;
import com.todoapp.entity.Task.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "Task description is required")
    private String taskDescription;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Deadline is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deadline;
}
