package org.example.task_manager.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("task not found with id: " + id);
    }
}