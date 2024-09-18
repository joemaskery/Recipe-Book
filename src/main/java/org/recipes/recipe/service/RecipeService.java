package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.dto.IngredientInput;
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

    public List<RecipeEntity> getByUserId(final Integer userId) {
        List<RecipeEntity> userRecipes = this.recipeRepository.findAllByUserId(userId);
        LOG.debug("[RecipeService] Found {} recipes for User {}", userRecipes.size(), userId);
        return userRecipes;
    }

    public RecipeEntity addRecipe(final AddRecipeRequest request) {
        LOG.debug("[RecipeService] Saving recipe: {}", request);
        return this.recipeRepository.save(toRecipe(request));
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

}
