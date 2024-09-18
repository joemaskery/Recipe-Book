package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.AddIngredientRequest;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class IngredientReferenceService {

    private final IngredientReferenceRepository ingredientRepository;

    public IngredientEntity addIngredient(final AddIngredientRequest request) {
        LOG.debug("[IngredientReferenceService] Saving ingredient: {}", request);
        return ingredientRepository.save(toIngredient(request));
    }

    public IngredientEntity toIngredient(final AddIngredientRequest request) {
        return IngredientEntity.builder()
                .name(request.getName())
                .category(request.getCategory())
                .userId(request.getUserId())
                .allUsers(false)
                .build();
    }

}
