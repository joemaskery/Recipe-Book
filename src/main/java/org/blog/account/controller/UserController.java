package org.blog.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.account.entity.User;
import org.blog.account.service.UserService;
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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        LOG.info("[UserController] Received request to add user - executing addUser");
        return ResponseEntity.ok(this.userService.addUser(user));
    }

    @DeleteMapping("/delete/userId")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "userId") Integer userId) {
        LOG.info("[UserController] Received request to delete user {}", userId);
        this.userService.deleteUser(userId);
        return ResponseEntity.ok("Successfully deleted user " + userId);
    }

}
