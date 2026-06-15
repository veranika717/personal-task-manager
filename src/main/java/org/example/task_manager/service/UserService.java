package org.example.task_manager.service;

import org.example.task_manager.dto.UserDTO;
import org.example.task_manager.exception.UserNotFoundException;
import org.example.task_manager.model.User;
import org.example.task_manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserDTO dto) {
        log.info("Creating user username={}", dto.getUsername());
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAll() {
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
        return userRepository.save(existing);
    }
}