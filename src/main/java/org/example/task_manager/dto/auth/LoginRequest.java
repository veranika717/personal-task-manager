package org.example.task_manager.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}