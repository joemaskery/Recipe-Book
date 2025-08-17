package org.recipes.user.repository;

import org.recipes.user.dto.UserDetailsAndStats;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.dto.UserEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntityId> findUserIdByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT new org.recipes.user.dto.UserDetailsAndStats(
                u.userId,
                u.firstName,
                u.secondName,
                u.email,
                u.avatar,
                u.createdDate,
                COUNT(DISTINCT r.recipeId),
                COUNT(DISTINCT i.ingredientRefId))
            FROM UserEntity u
            LEFT JOIN RecipeEntity r ON u.userId = r.userId
            LEFT JOIN RecipeIngredientEntity i ON r.recipeId = i.recipeId
            WHERE u.email = :email
            GROUP BY u.userId, u.firstName, u.secondName, u.email, u.createdDate
            """)
    Optional<UserDetailsAndStats> findUserWithStatsByEmail(String email);
}
