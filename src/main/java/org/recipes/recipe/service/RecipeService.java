package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.dto.IngredientInput;
import org.recipes.recipe.entity.Ingredient;
import org.recipes.recipe.entity.Recipe;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getByUserId(final Integer userId) {
        List<Recipe> userRecipes = this.recipeRepository.findAllByUserId(userId);
        LOG.debug("[RecipeService] Found {} recipes for User {}", userRecipes.size(), userId);
        return userRecipes;
    }

    public Recipe addRecipe(final AddRecipeRequest request) {
        LOG.debug("[RecipeService] Saving recipe: {}", request);
        final var result = this.recipeRepository.save(toRecipe(request));
        final var result2 = this.recipeRepository.findById(result.getRecipeId()).get();
        return result;
    }

    private Recipe toRecipe(final AddRecipeRequest request) {
        return Recipe.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .weblink(request.getWeblink())
                .ingredients(toIngredients(request.getIngredients()))
                .build();
    }

    private List<Ingredient> toIngredients(final List<IngredientInput> inputs) {
        if (inputs == null) {
            return List.of();
        }

        return inputs.stream()
                .map(this::toIngredient)
                .toList();
    }

    private Ingredient toIngredient(final IngredientInput input) {
        return Ingredient.builder()
                .name(input.getName())
                .quantity(input.getQuantity())
                .quantityType(input.getQuantityType())
                .build();
    }


}
