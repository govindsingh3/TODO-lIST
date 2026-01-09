package com.todoapp.controller;

import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.entity.Task.Priority;
import com.todoapp.entity.Task.Status;
import com.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskDTO response = taskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(
        @RequestParam(required = false) Priority priority,
        @RequestParam(required = false) Status status,
        @RequestParam(required = false) LocalDateTime deadlineFrom,
        @RequestParam(required = false) LocalDateTime deadlineTo,
        @RequestParam(required = false, defaultValue = "deadline") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        List<TaskDTO> tasks = taskService.getAllTasks(priority, status, deadlineFrom, deadlineTo, sortBy, sortOrder);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
        @PathVariable Long id,
        @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskDTO updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
