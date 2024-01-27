package org.blog.account.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.account.entity.User;
import org.blog.account.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {
        LOG.debug("Saving user: {}", user);
        return this.userRepository.save(user);
    }

}
