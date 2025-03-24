package org.recipes.user.repository;

import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.dto.UserEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntityId> findUserIdByEmail(String email);

}
