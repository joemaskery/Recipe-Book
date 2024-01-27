package org.blog.account.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.account.entity.User;
import org.blog.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            LOG.debug("User not found with userId: {}", userId);
            throw new IllegalStateException("User not found for ID: " + userId);
        } else {
            LOG.debug("Found User with userId: {}", userId);
            return userOptional.get();
        }
    }

    public User addUser(User user) {
        LOG.debug("Saving user: {}", user);
        return this.userRepository.save(user);
    }

}
