package org.recipes.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String INVALID_FIRST_NAME_MESSAGE = "First Name must not be blank";
    private static final String INVALID_SECOND_NAME_MESSAGE = "Second Name must not be blank";
    private static final String INVALID_EMAIL_MESSAGE = "Invalid email address";
    private static final String INVALID_PASSWORD_MESSAGE = "Password must not be blank";
    private static final String PASSWORDS_DONT_MATCH_MESSAGE = "Passwords do not match";

    @Mock UserRepository userRepository;
    @InjectMocks UserService underTest;

    @Test
    void getUser_throws_exception_for_unknown_id() {
        // given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.getUser(1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found for ID: 1");
    }

    @Test
    void deleteUser_throws_exception_for_unknown_id() {
        // given
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.deleteUser(1))
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
        assertThatThrownBy(() -> underTest.updateUser(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found for ID: 1");
    }

    @Test
    void addUser_correctly_validates_requests() {
        // given
        final var addUserRequest = AddUserRequest.builder()
                .firstName(" ")
                .secondName(" ")
                .email("invalid-email@email")
                .password1(" ")
                .password2("password2")
                .build();

        // when, then
        assertThatThrownBy(() -> underTest.addUser(addUserRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Invalid request to add user: [%s, %s, %s, %s, %s]",
                        INVALID_FIRST_NAME_MESSAGE, INVALID_SECOND_NAME_MESSAGE, INVALID_EMAIL_MESSAGE,
                        INVALID_PASSWORD_MESSAGE, PASSWORDS_DONT_MATCH_MESSAGE));
    }

}
