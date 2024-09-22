package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.AddIngredientRequest;
import org.recipes.recipe.dto.response.ReferenceIngredient;
import org.recipes.recipe.service.IngredientReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ingredient")
@AllArgsConstructor
@Slf4j
public class IngredientReferenceController {

    private final IngredientReferenceService ingredientReferenceService;

    @GetMapping("get-for-user/{userId}")
    public ResponseEntity<List<ReferenceIngredient>> getUserIngredients(@PathVariable final Integer userId) {
        LOG.info("Received request to get ingredients for user {}", userId);
        return ResponseEntity.ok(ingredientReferenceService.getAllForUser(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<ReferenceIngredient> addIngredient(@RequestBody final AddIngredientRequest request) {
        LOG.info("Received request to save ingredient: {}", request);
        return ResponseEntity.ok(ingredientReferenceService.addIngredient(request));
    }

}
