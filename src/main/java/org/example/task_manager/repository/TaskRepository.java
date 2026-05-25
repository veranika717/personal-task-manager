package org.example.task_manager.repository;

import org.example.task_manager.model.Task;
import org.example.task_manager.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByDeadlineBetween(LocalDateTime start, LocalDateTime end);

    List<Task> findByStatus(Status status);

    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title, String description);
}