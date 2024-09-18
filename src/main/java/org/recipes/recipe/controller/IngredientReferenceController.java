package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddIngredientRequest;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.service.IngredientReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ingredient")
@AllArgsConstructor
@Slf4j
public class IngredientReferenceController {

    private final IngredientReferenceService ingredientReferenceService;

    @PostMapping("/add")
    public ResponseEntity<IngredientEntity> addIngredient(@RequestBody final AddIngredientRequest request) {
        LOG.info("Received request to save ingredient: {}", request);
        return ResponseEntity.ok(ingredientReferenceService.addIngredient(request));
    }

}
