package org.recipes.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.recipes.commons.exception.NotFoundException;
import org.recipes.commons.exception.UserValidationException;
import org.recipes.auth.security.JwtHelper;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
import org.recipes.user.dto.UserDetailsAndStats;
import org.recipes.user.dto.UserStats;
import org.recipes.user.dto.UserWithStats;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.recipes.user.repository.dto.UserEntityId;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserWithStats getUserStatsByToken(final String token) {
        LOG.trace("Attempting to retrieve user recipes by token: {}", token);
        final String userEmail = JwtHelper.extractUsernameWithBearer(token);
        LOG.trace("Extracted user email: {}", userEmail);

        final UserDetailsAndStats userDetails = userRepository.findUserWithStatsByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return toUserWithStats(userDetails);
    }

    public User getUser(final Integer userId) {
        return mapOptionalEntityToUser(String.format("findById(%s)", userId),
                () -> userRepository.findById(userId));
    }

    public List<User> getAllUsers() {
        LOG.debug("[UserService] Returning all users");
        return userRepository.findAll().stream()
                .map(this::mapToUser)
                .toList();
    }

    public User addUser(final AddUserRequest request) {
        validateNewEmailIsValid(request.getEmail());
        LOG.debug("[UserService] Saving user: {}", request);
        final UserEntity savedUser = addNewUser(request);
        return mapToUser(savedUser);
    }

    public void deleteUser(final Integer userId) {
        final Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("[UserService] User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            userRepository.deleteById(userId);
            LOG.debug("[UserService] User with userId {} was found and deleted", userId);
        }
    }

    @Transactional
    public User updateUser(final UpdateUserRequest request) {
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

    public Integer getUserIdByToken(final String token) {
        LOG.trace("[UserService] Attempting to extract user email from token: {}", token);
        final String userEmail = JwtHelper.extractUsernameWithBearer(token);
        LOG.trace("[UserService] Extracted user email: {}", userEmail);

        final UserEntityId userId = userRepository.findUserIdByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        LOG.trace("[UserService] Retrieved userId {} for email {}", userId, userEmail);
        return userId.userId();
    }

    private User mapOptionalEntityToUser(final String description, final Supplier<Optional<UserEntity>> function) {
        final Optional<UserEntity> userEntityOptional = function.get();

        if (userEntityOptional.isEmpty()) {
            LOG.info("[UserService] User not found with supplier: {}", description);
            throw new NotFoundException("User not found");

        } else {
            LOG.info("[UserService] Found User with supplier: {}", description);
            return mapToUser(userEntityOptional.get());
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

    private void updateUserFields(final UserEntity user, final UpdateUserRequest request) {
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

    private UserEntity addNewUser(final AddUserRequest request) {
        return this.userRepository.save(UserEntity.builder()
                .firstName(request.getFirstName())
                .secondName(request.getSecondName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());
    }

    private void validateNewEmailIsValid(final String email) {
        LOG.debug("[UserService] Validating email is a valid new email: {}", email);
        List<String> validationErrors = new ArrayList<>();

        if (!EmailValidator.getInstance().isValid(email)) {
            validationErrors.add("Invalid email address");

        } else if (emailAlreadyInUse(email)) {
            validationErrors.add("Email address already in use");
        }

        if (!validationErrors.isEmpty()) {
            LOG.error("[UserService] addUserRequest failed validation with errors: {}", validationErrors);
            throw new UserValidationException(validationErrors.getFirst());
        }
    }

    private boolean emailAlreadyInUse(final String email) {
        return userRepository.existsByEmail(email);
    }

    private UserWithStats toUserWithStats(final UserDetailsAndStats userDetails) {
        return UserWithStats.builder()
                .userId(userDetails.getUserId())
                .firstName(userDetails.getFirstName())
                .secondName(userDetails.getSecondName())
                .email(userDetails.getEmail())
                .userStats(UserStats.builder()
                        .dateJoined(userDetails.getDateJoined())
                        .recipes(userDetails.getRecipes())
                        .ingredients(userDetails.getIngredients())
                        .build())
                .build();
    }
}
