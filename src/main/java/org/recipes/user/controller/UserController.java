package org.recipes.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.user.dto.*;
import org.recipes.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "userId") final Integer userId) {
        LOG.info("[UserController] Received request to get user with ID: {}", userId);
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestHeader(name="Authorization") final String token) {
        LOG.info("[UserController] Received request to get user details from auth token");
        return ResponseEntity.ok(this.userService.getUserByToken(token));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        LOG.info("[UserController] Received request to get all users");
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") final Integer userId) {
        LOG.info("[UserController] Received request to delete user {}", userId);
        this.userService.deleteUser(userId);
        return ResponseEntity.ok("Successfully deleted user " + userId);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody final UpdateUserRequest request) {
        LOG.info("[UserController] Received request to update user {}", request.getUserId());
        return ResponseEntity.ok(this.userService.updateUser(request));
    }
}
