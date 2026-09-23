package org.recipes.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
import org.recipes.user.dto.UserWithStats;
import org.recipes.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/get-with-stats")
    public ResponseEntity<UserWithStats> getUserWithStats() {
        LOG.info("[UserController] Received request to get stats and details of logged in user");
        return ResponseEntity.ok(this.userService.getLoggedInUserStats());
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
