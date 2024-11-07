package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.AddRecipeRequest;
import org.recipes.recipe.dto.response.UserRecipe;
import org.recipes.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/get-for-user/{userId}")
    public ResponseEntity<List<UserRecipe>> getUserRecipes(@PathVariable Integer userId) {
        LOG.info("[RecipeController] Received request to get user {} recipes", userId);
        return ResponseEntity.ok(this.recipeService.getByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<UserRecipe> addRecipe(@RequestBody AddRecipeRequest request) {
        LOG.info("[RecipeController] Received request to add recipe {}", request.getName());
        return ResponseEntity.ok(this.recipeService.addRecipe(request));
    }

}
