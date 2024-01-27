package org.blog.account.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.account.entity.User;
import org.blog.account.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUser(@PathVariable (value = "userId") Integer userId) {
        LOG.info("Received request to get user with ID: {}", userId);
        return ResponseEntity.ok(this.userService.getUser(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        LOG.info("Received request to add user - executing addUser");
        return ResponseEntity.ok(this.userService.addUser(user));
    }

}
