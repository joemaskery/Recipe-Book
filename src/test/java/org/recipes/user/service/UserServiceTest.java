package org.recipes.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.auth.security.JwtHelper;
import org.recipes.auth.service.CurrentUserService;
import org.recipes.commons.exception.NotFoundException;
import org.recipes.commons.exception.UserValidationException;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String INVALID_EMAIL_MESSAGE = "Invalid email address";
    private static final String EMAIL_ALREADY_IN_USE_MESSAGE = "Email address already in use";
    private static final String EMAIL_ADDRESS = "test-email@email.com";

    @Mock UserRepository userRepository;
    @Mock CurrentUserService currentUserService;
    @InjectMocks UserService userService;

    @Test
    void getLoggedInUserStats_throws_exception_for_unknown_user() {
        // given
        when(currentUserService.getUserEmail()).thenReturn(EMAIL_ADDRESS);
        when(userRepository.findUserWithStatsByEmail(EMAIL_ADDRESS)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> userService.getLoggedInUserStats())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void getUser_throws_exception_for_unknown_id() {
        // given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> userService.getUser(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void deleteUser_throws_exception_for_unknown_id() {
        // given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> userService.deleteUser(1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found for ID: 1");
    }

    @Test
    void updateUser_throws_exception_for_unknown_id() {
        // given
        final UpdateUserRequest request = UpdateUserRequest.builder()
                .userId(1)
                .build();
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> userService.updateUser(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found for ID: 1");
    }

    @Test
    void addUser_throws_exception_if_email_is_invalid() {
        // given
        final var addUserRequest = AddUserRequest.builder()
                .firstName(" ")
                .secondName(" ")
                .email("invalid-email@email")
                .password(" ")
                .build();

        // when, then
        assertThatThrownBy(() -> userService.addUser(addUserRequest))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(INVALID_EMAIL_MESSAGE);
    }

    @Test
    void addUser_throws_exception_if_email_already_in_use() {
        // given
        final var addUserRequest = AddUserRequest.builder()
                .email(EMAIL_ADDRESS)
                .build();

        when(userRepository.existsByEmail(EMAIL_ADDRESS)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> userService.addUser(addUserRequest))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(EMAIL_ALREADY_IN_USE_MESSAGE);
    }
}
