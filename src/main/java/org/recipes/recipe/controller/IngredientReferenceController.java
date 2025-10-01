package org.recipes.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.AddIngredientRequest;
import org.recipes.recipe.dto.response.ReferenceIngredient;
import org.recipes.recipe.dto.response.ReferenceIngredientsResponse;
import org.recipes.recipe.service.IngredientReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ingredient")
@AllArgsConstructor
@Slf4j
@CrossOrigin
public class IngredientReferenceController {

    private final IngredientReferenceService ingredientReferenceService;

    @GetMapping("get-for-user")
    public ResponseEntity<ReferenceIngredientsResponse> getUserIngredients(
            @RequestHeader(name="Authorization") final String token) {
        LOG.info("Received request to get reference ingredients");
        return ResponseEntity.ok(ingredientReferenceService.getAllForUser(token));
    }

    @PostMapping("/add")
    public ResponseEntity<ReferenceIngredient> addIngredient(@RequestBody final AddIngredientRequest request) {
        LOG.info("Received request to save ingredient: {}", request);
        return ResponseEntity.ok(ingredientReferenceService.addIngredient(request));
    }

}
