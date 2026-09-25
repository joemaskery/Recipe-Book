package org.recipes.auth.service;

import org.recipes.auth.model.MyUserDetails;
import org.recipes.commons.exception.NoLoggedInUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {

    public Integer getUserId() {
        return getUser().getUserId();
    }

    public String getUserEmail() {
        return getUser().getEmail();
    }

    private MyUserDetails getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof MyUserDetails user)) {
            throw new NoLoggedInUserException("Couldn't find authenticated user");
        }

        return user;
    }
}
