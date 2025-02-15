package org.recipes.recipe.repository;

import org.recipes.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    List<RecipeEntity> findAllByUserId(Integer userId);

    @Query("SELECT re FROM RecipeEntity re "
            + "JOIN UserEntity ue ON re.userId = ue.userId "
            + "WHERE ue.email = :email")
    List<RecipeEntity> findAllByUserEmail(String email);

}
