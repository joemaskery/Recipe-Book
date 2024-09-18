package org.recipes.recipe.repository;

import org.recipes.recipe.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    List<RecipeEntity> findAllByUserId(Integer userId);

}
