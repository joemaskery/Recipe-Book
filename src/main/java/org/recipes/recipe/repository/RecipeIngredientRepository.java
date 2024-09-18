package org.recipes.recipe.repository;

import org.recipes.recipe.entity.RecipeIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredientEntity, Integer> {

    List<RecipeIngredientEntity> findAllByRecipeId(Integer recipeId);

}
