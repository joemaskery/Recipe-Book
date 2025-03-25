package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.AddIngredientRequest;
import org.recipes.recipe.dto.response.ReferenceIngredient;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.recipes.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class IngredientReferenceService {

    private final IngredientReferenceRepository ingredientRepository;
    private final UserService userService;

    public ReferenceIngredient addIngredient(final AddIngredientRequest request) {
        LOG.debug("[IngredientReferenceService] Saving ingredient: {}", request);
        final IngredientEntity savedIngredient = ingredientRepository.save(toIngredientEntity(request));
        LOG.debug("[IngredientReferenceService] Saved ingredient with ID {}", savedIngredient.getReferenceId());
        return toReferenceIngredient(savedIngredient);
    }

    public List<ReferenceIngredient> getAllForUser(final String userToken) {
        final Integer userId = userService.getUserIdByToken(userToken);
        final List<IngredientEntity> ingredientEntities = ingredientRepository
                .findAllByUserIdEqualsOrAllUsersIsTrue(userId);
        LOG.debug("Found {} ingredients for user {}", ingredientEntities.size(), userId);
        return toReferenceIngredients(ingredientEntities);
    }

    private ReferenceIngredient toReferenceIngredient(final IngredientEntity entity) {
        return ReferenceIngredient.builder()
                .id(entity.getReferenceId())
                .name(entity.getName())
                .category(entity.getCategory())
                .allUsers(entity.isAllUsers())
                .build();
    }

    private List<ReferenceIngredient> toReferenceIngredients(final List<IngredientEntity> ingredientEntities) {
        return ingredientEntities.stream()
                .map(this::toReferenceIngredient)
                .toList();
    }

    private IngredientEntity toIngredientEntity(final AddIngredientRequest request) {
        return IngredientEntity.builder()
                .name(request.getName())
                .category(request.getCategory())
                .userId(request.getUserId())
                .allUsers(false)
                .build();
    }

}
