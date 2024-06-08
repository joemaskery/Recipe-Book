package org.recipes.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.entity.User;
import org.recipes.user.repository.UserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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

    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

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
        return this.addNewUser(request);
    }

    private User addNewUser(AddUserRequest request) {
        return this.userRepository.save(User.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .email(request.getEmail())
                .password(PASSWORD_ENCODER.encode(request.getPassword1()))
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

    public boolean userPasswordMatches(Integer userId, String userPassword) {
        LOG.debug("Checking user password for: userId={}, userPassword={}", userId, userPassword);
        Optional<User> user = this.userRepository.findById(userId);

        if (user.isEmpty()) {
            LOG.error("Can't check password for user {} as they don't exist", userId);
           throw new IllegalStateException("User " + userId + " doesn't exist");
        }

        return PASSWORD_ENCODER.matches(userPassword, user.get().getPassword());
    }

}
