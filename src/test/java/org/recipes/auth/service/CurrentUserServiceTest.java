package org.recipes.auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.auth.model.MyUserDetails;
import org.recipes.commons.exception.NoLoggedInUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    CurrentUserService currentUserService = new CurrentUserService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserId_should_return_authenticated_user_id() {
        // given
        final MyUserDetails user = mock(MyUserDetails.class);
        when(user.getUserId()).thenReturn(123);

        final Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // when, then
        assertThat(currentUserService.getUserId()).isEqualTo(123);
    }

    @Test
    void getUserId_throws_exception_when_no_user_is_authenticated() {
        // given
        SecurityContextHolder.clearContext();
        // when, then
        assertThatThrownBy(() -> currentUserService.getUserId())
                .isInstanceOf(NoLoggedInUserException.class)
                .hasMessage("Couldn't find authenticated user");
    }

    @Test
    void getUserEmail_should_return_authenticated_user_email() {
        // given
        final MyUserDetails user = mock(MyUserDetails.class);
        when(user.getEmail()).thenReturn("test@example.com");

        final Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // when, then
        assertThat(currentUserService.getUserEmail())
                .isEqualTo("test@example.com");
    }

    @Test
    void getUserEmail_throws_exception_when_no_user_is_authenticated() {
        // given
        SecurityContextHolder.clearContext();
        // when, then
        assertThatThrownBy(() -> currentUserService.getUserEmail())
                .isInstanceOf(NoLoggedInUserException.class)
                .hasMessage("Couldn't find authenticated user");
    }
}
