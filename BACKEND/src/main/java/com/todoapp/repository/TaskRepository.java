package com.todoapp.repository;

import com.todoapp.entity.Task;
import com.todoapp.entity.Task.Priority;
import com.todoapp.entity.Task.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.id = :id")
    Optional<Task> findByIdWithUser(@Param("id") Long id);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId")
    List<Task> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId AND t.priority = :priority")
    List<Task> findByUserIdAndPriority(@Param("userId") Long userId, @Param("priority") Priority priority);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId AND t.status = :status")
    List<Task> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId AND t.deadline BETWEEN :startDate AND :endDate")
    List<Task> findByUserIdAndDeadlineBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT t FROM Task t JOIN FETCH t.user WHERE t.user.id = :userId AND t.priority = :priority AND t.status = :status")
    List<Task> findByUserIdAndPriorityAndStatus(
        @Param("userId") Long userId,
        @Param("priority") Priority priority,
        @Param("status") Status status
    );
}
