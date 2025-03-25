package org.recipes.testutils;

import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHelper {

    public static final UserEntity USER_1 = UserEntity.builder()
            .firstName("name1")
            .secondName("surname1")
            .email("email1@domain.com")
            .build();

    public static final UserEntity USER_2 = UserEntity.builder()
            .firstName("name2")
            .secondName("surname2")
            .email("email2@domain.com")
            .build();

    @Autowired UserRepository userRepository;

    public void saveUsers() {
        userRepository.saveAll(List.of(USER_1, USER_2));
    }
}
