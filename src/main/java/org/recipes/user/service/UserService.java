package org.recipes.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User getUser(final Integer userId) {
        final Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            LOG.debug("[UserService] Found User with userId: {}", userId);
            return mapToUser(userOptional.get());
        }
    }

    public List<User> getAllUsers() {
        LOG.debug("[UserService] Returning all users");
        return userRepository.findAll().stream()
                .map(this::mapToUser)
                .toList();
    }

    public User addUser(AddUserRequest request) {
        validateAddUserRequest(request);
        LOG.debug("[UserService] Saving user: {}", request);
        final UserEntity savedUser = addNewUser(request);
        return mapToUser(savedUser);
    }

    public void deleteUser(Integer userId) {
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            userRepository.deleteById(userId);
            LOG.debug("[UserService] User with userId {} was found and deleted", userId);
        }
    }

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        LOG.debug("[UserService] Update user request: {}", request);
        final Optional<UserEntity> userOptional = this.userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", request.getUserId());
            throw new IllegalStateException("User not found for ID: " + request.getUserId());
        } else {
            final UserEntity user = userOptional.get();
            updateUserFields(user, request);
            return mapToUser(user);
        }
    }


    private User mapToUser(final UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .secondName(userEntity.getSecondName())
                .email(userEntity.getEmail())
                .build();
    }

    private void updateUserFields(UserEntity user, UpdateUserRequest request) {
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

    private UserEntity addNewUser(AddUserRequest request) {
        return this.userRepository.save(UserEntity.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword1()))
                .build());
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

}
