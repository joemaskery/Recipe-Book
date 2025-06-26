package org.recipes.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.auth.exception.UserValidationException;
import org.recipes.auth.security.JwtHelper;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.recipes.user.repository.dto.UserEntityId;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String INVALID_EMAIL_MESSAGE = "Invalid email address";
    private static final String EMAIL_ALREADY_IN_USE_MESSAGE = "Email address already in use";

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @Test
    void getUser_throws_exception_for_unknown_id() {
        // given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> userService.getUser(1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found for ID: 1");
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
                .firstName("firstName")
                .secondName("secondName")
                .email("email@domain.com")
                .password("password")
                .build();

        when(userRepository.existsByEmail("email@domain.com")).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> userService.addUser(addUserRequest))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(EMAIL_ALREADY_IN_USE_MESSAGE);
    }

    @Test
    void getUserIdByToken_returns_user_id() {
        // given
        final String token = String.format("Bearer %s", JwtHelper.generateToken("test-email@email.com"));
        when(userRepository.findUserIdByEmail("test-email@email.com")).thenReturn(Optional.of(new UserEntityId(123)));
        // when, then
        assertThat(userService.getUserIdByToken(token)).isEqualTo(123);
    }

    @Test
    void getUserIdByToken_throws_exception_if_email_does_not_exist() {
        // given
        final String token = String.format("Bearer %s", JwtHelper.generateToken("test-email@email.com"));
        // when, then
        assertThatThrownBy(() -> userService.getUserIdByToken(token))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with email: test-email@email.com");
    }
}
