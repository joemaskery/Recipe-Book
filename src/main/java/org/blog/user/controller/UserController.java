package org.blog.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.user.dto.AddUserRequest;
import org.blog.user.dto.UpdateUserRequest;
import org.blog.user.entity.User;
import org.blog.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUser(@PathVariable(value = "userId") Integer userId) {
        LOG.info("[UserController] Received request to get user with ID: {}", userId);
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        LOG.info("[UserController] Received request to get all users");
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest request) {
        LOG.info("[UserController] Received request to add user {}", request.getEmail());
        return ResponseEntity.ok(this.userService.addUser(request));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") Integer userId) {
        LOG.info("[UserController] Received request to delete user {}", userId);
        this.userService.deleteUser(userId);
        return ResponseEntity.ok("Successfully deleted user " + userId);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest request) {
        LOG.info("[UserController] Received request to update user {}", request.getUserId());
        return ResponseEntity.ok(this.userService.updateUser(request));
    }
}
