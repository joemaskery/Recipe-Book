package org.blog.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.coyote.BadRequestException;
import org.blog.user.dto.AddUserRequest;
import org.blog.user.dto.UpdateUserRequest;
import org.blog.user.entity.User;
import org.blog.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public User addUser(AddUserRequest request) {
        this.validateAddUserRequest(request);
        LOG.debug("[UserService] Saving user: {}", request);
        return this.userRepository.save(this.mapToUser(request));
    }

    private void validateAddUserRequest(AddUserRequest request) {
        LOG.debug("[UserService] Validating addUserRequest: {}", request);
        List<String> validationErrors = new ArrayList<>();
        if (StringUtils.isBlank(request.getFirstName())) {
            validationErrors.add("First Name must not be blank");
        }
        if (StringUtils.isBlank(request.getSecondName())) {
            validationErrors.add("Second Name must not be blank");
        }
        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            validationErrors.add("Invalid email address");
        }
        if (StringUtils.isBlank(request.getPassword1())) {
            validationErrors.add("Password must not be blank");
        }
        if (!Objects.equals(request.getPassword1(), request.getPassword2())) {
            validationErrors.add("Passwords do not match");
        }

        if (!validationErrors.isEmpty()) {
            LOG.error("[UserService] addUserRequest failed validation with errors: {}", validationErrors);
            throw new IllegalStateException("Invalid request to add user: " + validationErrors);
        }
    }

    private User mapToUser(AddUserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .email(request.getEmail())
                .build();
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
