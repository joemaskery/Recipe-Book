package org.recipes.auth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.auth.service.AuthService;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.auth.dto.LoginRequest;
import org.recipes.auth.dto.LoginResponse;
import org.recipes.user.dto.User;
import org.recipes.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody final AddUserRequest request) {
        LOG.info("[UserController] Received request to add user {}", request.getEmail());
        return ResponseEntity.ok(this.userService.addUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody final LoginRequest request) {
        LOG.info("[UserController] Received request to login for user {}", request.getEmail());
        return ResponseEntity.ok(this.authService.login(request.getEmail(), request.getPassword()));
    }

}
