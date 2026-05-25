package org.example.task_manager.service;

import org.example.task_manager.dto.TaskDTO;
import org.example.task_manager.exception.TaskNotFoundException;
import org.example.task_manager.exception.UserNotFoundException;
import org.example.task_manager.model.Status;
import org.example.task_manager.model.Task;
import org.example.task_manager.model.User;
import org.example.task_manager.repository.TaskRepository;
import org.example.task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task create(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        task.setUser(user);
        task.setStatus(Status.UPCOMING);
        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void delete(Long id) {
        Task task = getById(id);
        taskRepository.deleteById(id);
    }

    public Task markDone(Long id) {
        Task task = getById(id);
        task.setStatus(Status.DONE);
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskDTO dto) {
        Task task = getById(id);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        return taskRepository.save(task);
    }

    public List<Task> search(String keyword, Status status) {
        if (keyword != null && !keyword.isEmpty()) {
            return taskRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        }
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll();
    }
    public List<Task> getTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getTasks();
    }
}