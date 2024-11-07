package org.recipes.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.auth.security.JwtHelper;
import org.recipes.user.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public LoginResponse login(final String email, final String password) {
        LOG.debug("Authenticating user: {}", email);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        final String token = JwtHelper.generateToken(email);
        LOG.debug("Successfully authenticated user {}", email);
        return new LoginResponse(email, token);
    }

}
