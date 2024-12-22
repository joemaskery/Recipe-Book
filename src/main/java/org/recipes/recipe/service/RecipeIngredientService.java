package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public List<IngredientSummary> getIngredientsForRecipeIds(final List<Integer> recipeIds) {
        return recipeIngredientRepository.findAllByRecipeIdIn(recipeIds);
    }

}
