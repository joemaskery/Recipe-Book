package org.blog.account.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.blog.account.dto.UpdateUserRequest;
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

    public void deleteUser(Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            this.userRepository.deleteById(userId);
            LOG.debug("[UserService] User with userId {} was found and deleted", userId);
        }
    }

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        LOG.debug("[UserService] Update user request: {}", request);
        Optional<User> userOptional = this.userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", request.getUserId());
            throw new IllegalStateException("User not found for ID: " + request.getUserId());
        } else {
            final User user = userOptional.get();
            this.updateUserFields(user, request);
            return user;
        }
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        if (!StringUtils.isBlank(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
            LOG.trace("[UserService] User first name was set to {}", request.getFirstName());
        }

        if (!StringUtils.isBlank(request.getSecondName())) {
            user.setSecondName(request.getSecondName());
            LOG.trace("[UserService] User second name was set to {}", request.getSecondName());
        }

        if (!StringUtils.isBlank(request.getEmail())) {
            user.setEmail(request.getEmail());
            LOG.trace("[UserService] User email was set to {}", request.getEmail());
        }
    }

}
