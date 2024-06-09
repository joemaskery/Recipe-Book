package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.entity.Recipe;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getByUserId(Integer userId) {
        List<Recipe> userRecipes = this.recipeRepository.findAllByUserId(userId);
        LOG.debug("[RecipeService] Found {} recipes for User {}", userRecipes.size(), userId);
        return userRecipes;
    }

    public Recipe addRecipe(AddRecipeRequest request) {
        LOG.debug("[RecipeService] Saving recipe: {}", request);
        return this.recipeRepository.save(Recipe.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .weblink(request.getWeblink())
                .build()
        );
    }

}
