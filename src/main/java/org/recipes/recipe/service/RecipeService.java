package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.dto.IngredientInput;
import org.recipes.recipe.dto.RecipeIngredient;
import org.recipes.recipe.dto.UserRecipe;
import org.recipes.recipe.entity.RecipeIngredientEntity;
import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<UserRecipe> getByUserId(final Integer userId) {
        List<RecipeEntity> recipeEntities = this.recipeRepository.findAllByUserId(userId);
        LOG.debug("[RecipeService] Found {} recipes for User {}", recipeEntities.size(), userId);
        return mapToUserRecipes(recipeEntities);
    }

    public UserRecipe addRecipe(final AddRecipeRequest request) {
        LOG.debug("[RecipeService] Saving recipe: {}", request);
        final RecipeEntity savedRecipe = recipeRepository.save(toRecipe(request));
        return mapToUserRecipe(savedRecipe);
    }

    private RecipeEntity toRecipe(final AddRecipeRequest request) {
        return RecipeEntity.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .weblink(request.getWeblink())
                .recipeIngredients(toIngredients(request.getIngredients()))
                .build();
    }

    private List<RecipeIngredientEntity> toIngredients(final List<IngredientInput> inputs) {
        if (inputs == null) {
            return List.of();
        }

        return inputs.stream()
                .map(this::toIngredient)
                .toList();
    }

    private RecipeIngredientEntity toIngredient(final IngredientInput input) {
        return RecipeIngredientEntity.builder()
                .quantity(input.getQuantity())
                .quantityType(input.getQuantityType())
                .ingredientRefId(input.getIngredientRefId())
                .build();
    }

    private List<UserRecipe> mapToUserRecipes(final List<RecipeEntity> recipeEntities) {
        return recipeEntities.stream()
                .map(this::mapToUserRecipe)
                .toList();
    }

    private UserRecipe mapToUserRecipe(final RecipeEntity recipe) {
        return UserRecipe.builder()
                .recipeId(recipe.getRecipeId())
                .userId(recipe.getUserId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .weblink(recipe.getWeblink())
                .ingredients(toRecipeIngredients(recipe.getRecipeIngredients()))
                .build();
    }

    private List<RecipeIngredient> toRecipeIngredients(final List<RecipeIngredientEntity> ingredients) {
        return ingredients.stream()
                .map(ingredient -> RecipeIngredient.builder()
                        .name(ingredient.getName())
                        .category(ingredient.getCategory())
                        .quantity(ingredient.getQuantity())
                        .quantityType(ingredient.getQuantityType())
                        .build())
                .toList();
    }

}
