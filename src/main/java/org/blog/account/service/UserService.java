package org.blog.account.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.account.entity.User;
import org.blog.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            LOG.debug("[UserService] Found User with userId: {}", userId);
            return userOptional.get();
        }
    }

    public List<User> getAllUsers() {
        LOG.debug("[UserService] Returning all users");
        return this.userRepository.findAll();
    }

    public User addUser(User user) {
        LOG.debug("[UserService] Saving user: {}", user);
        return this.userRepository.save(user);
    }

}
