package org.recipes.recipe.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.recipe.repository.RecipeRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock RecipeRepository recipeRepository;

    @InjectMocks RecipeService recipeService;

    @Test
    void getRecipeById_throws_exception_if_no_recipe_found() {
        assertThatThrownBy(() -> recipeService.getRecipeById(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("No recipe found with ID 1");
    }
}
