package com.todoapp.service;

import com.todoapp.dto.CreateTaskRequest;
import com.todoapp.dto.TaskDTO;
import com.todoapp.entity.Task;
import com.todoapp.entity.Task.Priority;
import com.todoapp.entity.Task.Status;
import com.todoapp.entity.User;
import com.todoapp.repository.TaskRepository;
import com.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDTO createTask(CreateTaskRequest request) {
        User user = getCurrentUser();
        Task task = Task.builder()
            .taskDescription(request.getTaskDescription())
            .priority(request.getPriority())
            .status(request.getStatus())
            .deadline(request.getDeadline())
            .user(user)
            .build();
        Task savedTask = taskRepository.save(task);
        return TaskDTO.fromEntity(savedTask);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findByIdWithUser(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        validateUserOwnership(task);
        return TaskDTO.fromEntity(task);
    }

    public List<TaskDTO> getAllTasks(
        Priority priority,
        Status status,
        LocalDateTime deadlineFrom,
        LocalDateTime deadlineTo,
        String sortBy,
        String sortOrder
    ) {
        User user = getCurrentUser();
        List<Task> tasks;

        if (priority != null && status != null) {
            tasks = taskRepository.findByUserIdAndPriorityAndStatus(user.getId(), priority, status);
        } else if (priority != null) {
            tasks = taskRepository.findByUserIdAndPriority(user.getId(), priority);
        } else if (status != null) {
            tasks = taskRepository.findByUserIdAndStatus(user.getId(), status);
        } else if (deadlineFrom != null && deadlineTo != null) {
            tasks = taskRepository.findByUserIdAndDeadlineBetween(user.getId(), deadlineFrom, deadlineTo);
        } else {
            tasks = taskRepository.findByUserId(user.getId());
        }

        return sortTasks(tasks, sortBy, sortOrder);
    }

    public TaskDTO updateTask(Long id, CreateTaskRequest request) {
        Task task = taskRepository.findByIdWithUser(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        validateUserOwnership(task);

        task.setTaskDescription(request.getTaskDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDeadline(request.getDeadline());

        Task updatedTask = taskRepository.save(task);
        return TaskDTO.fromEntity(updatedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findByIdWithUser(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
        validateUserOwnership(task);
        taskRepository.delete(task);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void validateUserOwnership(Task task) {
        User currentUser = getCurrentUser();
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized access to task");
        }
    }

    private List<TaskDTO> sortTasks(List<Task> tasks, String sortBy, String sortOrder) {
        Comparator<Task> comparator;
        boolean isDesc = "desc".equalsIgnoreCase(sortOrder);

        if ("priority".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(task -> task.getPriority().ordinal());
        } else if ("status".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(task -> task.getStatus().ordinal());
        } else if ("deadline".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.nullsLast(Comparator.comparing(Task::getDeadline));
        } else {
            return tasks.stream().map(TaskDTO::fromEntity).collect(Collectors.toList());
        }

        if (isDesc) {
            comparator = comparator.reversed();
        }

        return tasks.stream()
            .sorted(comparator)
            .map(TaskDTO::fromEntity)
            .collect(Collectors.toList());
    }
}
