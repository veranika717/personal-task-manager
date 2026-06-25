package org.example.task_manager.dto.auth;

import org.example.task_manager.model.Role;

public record RegisterRequest(
        String username,
        String email,
        String password,
        Role role
) {
}