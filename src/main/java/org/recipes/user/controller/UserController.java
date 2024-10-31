package org.recipes.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
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
    public ResponseEntity<User> getUser(@PathVariable(value = "userId") final Integer userId) {
        LOG.info("[UserController] Received request to get user with ID: {}", userId);
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        LOG.info("[UserController] Received request to get all users");
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody final AddUserRequest request) {
        LOG.info("[UserController] Received request to add user {}", request.getEmail());
        return ResponseEntity.ok(this.userService.addUser(request));
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

    @GetMapping("/check-password")
    public ResponseEntity<Boolean> checkUserPassword(@RequestParam final Integer userId,
                                                     @RequestParam final String userPassword) {
        LOG.info("[UserController] Received request to check User {} password", userId);
        return ResponseEntity.ok(this.userService.userPasswordMatches(userId, userPassword));
    }

}
