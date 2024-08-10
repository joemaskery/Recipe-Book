package org.recipes.recipe.repository;

import org.recipes.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findAllByRecipeId(Integer recipeId);

}
