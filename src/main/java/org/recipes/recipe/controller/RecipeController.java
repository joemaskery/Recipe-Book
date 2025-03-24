package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.AddRecipeRequest;
import org.recipes.recipe.dto.response.UserRecipe;
import org.recipes.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/get-for-user/{userId}")
    public ResponseEntity<List<UserRecipe>> getUserRecipesByUserId(@PathVariable final Integer userId) {
        LOG.info("[RecipeController] Received request to get user {} recipes", userId);
        return ResponseEntity.ok(this.recipeService.getByUserId(userId));
    }

    @GetMapping("/get-for-user")
    public ResponseEntity<List<UserRecipe>> getUserRecipes(@RequestHeader(name="Authorization") final String token) {
        LOG.info("[RecipeController] Received request to get user recipes");
        return ResponseEntity.ok(this.recipeService.getByUserToken(token));
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<UserRecipe> getRecipeById(@PathVariable final Integer recipeId) {
        LOG.info("[RecipeController] Received request to get recipe by recipeId {}", recipeId);
        return ResponseEntity.ok(this.recipeService.getRecipeById(recipeId));
    }

    @PostMapping("/add")
    public ResponseEntity<UserRecipe> addRecipe(@RequestHeader(name="Authorization") final String token,
                                                @RequestBody final AddRecipeRequest request) {
        LOG.info("[RecipeController] Received request to add recipe {}", request.getName());
        return ResponseEntity.ok(this.recipeService.addRecipe(token, request));
    }

}
