package org.example.task_manager.service;

import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exception.TaskNotFoundException;
import org.example.task_manager.exception.UserNotFoundException;
import org.example.task_manager.model.Status;
import org.example.task_manager.model.Task;
import org.example.task_manager.model.User;
import org.example.task_manager.repository.TaskRepository;
import org.example.task_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task create(Task task, Long userId) {
        log.info("Creating task for userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found with id={}", userId);
                    return new UserNotFoundException(userId);
                });
        task.setUser(user);
        task.setStatus(Status.UPCOMING);
        Task saved = taskRepository.save(task);
        log.info("Task created with id={}", saved.getId());
        return saved;
    }

    @PreAuthorize("hasAnyRole('USER', 'MANAGER')")
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(this::recalcStatus);
        return tasks;
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void delete(Long id) {
        log.info("Deleting task id={}", id);
        Task task = getById(id);
        taskRepository.deleteById(id);
        log.info("Task deleted id={}", id);
    }

    public Task markDone(Long id) {
        log.info("Marking task done id={}", id);
        Task task = getById(id);
        task.setStatus(Status.DONE);
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskDTO dto) {
        log.info("Updating task id={}", id);
        Task task = getById(id);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        recalcStatus(task);
        return taskRepository.save(task);
    }

    public List<Task> search(String keyword, Status status) {
        log.debug("Searching tasks keyword={} status={}", keyword, status);
        List<Task> tasks;
        if (keyword != null && !keyword.isEmpty()) {
            tasks = taskRepository
                    .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else {
            tasks = taskRepository.findAll();
        }
        tasks.forEach(this::recalcStatus);
        return tasks;
    }

    public List<Task> getTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getTasks();
    }

    private void recalcStatus(Task task) {
        if (task.getStatus() == Status.DONE) {
            return;
        }
        if (task.getDeadline() != null
                && task.getDeadline().isBefore(LocalDateTime.now())) {
            task.setStatus(Status.OVERDUE);
        } else {
            task.setStatus(Status.UPCOMING);
        }
    }
}