package org.recipes.recipe.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.auth.service.CurrentUserService;
import org.recipes.recipe.dto.request.AddRecipeRequest;
import org.recipes.recipe.dto.request.IngredientInput;
import org.recipes.recipe.dto.response.RecipeIngredient;
import org.recipes.recipe.dto.response.UserRecipe;
import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.entity.RecipeIngredientEntity;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CurrentUserService currentUserService;

    public List<UserRecipe> getByUserId(final Integer userId) {
        List<RecipeEntity> recipeEntities = this.recipeRepository.findAllByUserId(userId);
        LOG.info("Found {} recipes for User {}", recipeEntities.size(), userId);
        return mapToUserRecipes(recipeEntities);
    }

    public List<UserRecipe> getLoggedInUserRecipes() {
        final Integer userId = currentUserService.getUserId();
        LOG.trace("Logged in user ID: {}", userId);

        LOG.info("Fetching recipes for user {}", userId);
        final List<RecipeEntity> recipeEntities = this.recipeRepository.findAllByUserId(userId);
        LOG.info("Found {} recipes for User {}", recipeEntities.size(), userId);
        return mapToUserRecipes(recipeEntities);
    }

    public UserRecipe getRecipeById(final Integer recipeId) {
        final RecipeEntity recipeEntity = this.recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("No recipe found with ID " + recipeId));

        LOG.debug("Found recipe with recipeId {}", recipeId);
        return mapToUserRecipe(recipeEntity);
    }

    @Transactional
    public UserRecipe addRecipeForLoggedInUser(final AddRecipeRequest request) {
        final Integer userId = currentUserService.getUserId();
        LOG.info("Saving recipe {} for user {}", request.getName(), userId);
        LOG.debug("Saving recipe: {}", request);
        final RecipeEntity savedRecipe = recipeRepository.save(toRecipe(userId, request));
        LOG.info("Saved new recipe with ID: {}", savedRecipe.getRecipeId());
        return mapToUserRecipe(savedRecipe);
    }

    private RecipeEntity toRecipe(final Integer userId, final AddRecipeRequest request) {
        return RecipeEntity.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .instructions(request.getInstructions())
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
                .instructions(recipe.getInstructions())
                .weblink(recipe.getWeblink())
                .createdDate(recipe.getCreatedDate() == null ? null : recipe.getCreatedDate().toLocalDate())
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
                        .quantityName(ingredient.getQuantityType() == null ? null :
                                ingredient.getQuantityType().getName())
                        .build())
                .toList();
    }

}
