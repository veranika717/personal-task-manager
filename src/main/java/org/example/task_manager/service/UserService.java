package org.example.task_manager.service;

import org.example.task_manager.dto.UserDTO;
import org.example.task_manager.exception.UserNotFoundException;
import org.example.task_manager.model.Role;
import org.example.task_manager.model.User;
import org.example.task_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserDTO dto) {
        log.info("Creating user username={}", dto.getUsername());
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.USER);
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @HandleAuthorizationDenied(handlerClass = UserAccessDeniedHandler.class)
    public List<User> getAllSecured() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        log.info("Deleting user id={}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found id={}", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("User deleted id={}", id);
    }

    public User update(Long id, UserDTO dto) {
        log.info("Updating user id={}", id);
        User existing = getById(id);
        existing.setUsername(dto.getUsername());
        existing.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null) {
            existing.setRole(dto.getRole());
        }
        return userRepository.save(existing);
    }
}