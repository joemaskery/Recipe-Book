package org.recipes.testutils;

import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.dto.AddRecipeRequest.AddRecipeRequestBuilder;
import org.recipes.recipe.dto.IngredientInput;
import org.recipes.recipe.model.QuantityType;

import java.util.List;

public class AddRecipeRequestTestBuilder {

    public static AddRecipeRequestBuilder addRecipeRequest(final Integer userId) {
        return AddRecipeRequest.builder()
                .userId(userId)
                .name("recipe-name")
                .description("recipe-description")
                .weblink("www.recipe-book.com")
                .ingredients(List.of(
                        ingredientInput("ingredient-1", 100.00, QuantityType.GRAM),
                        ingredientInput("ingredient-2", 200.00, QuantityType.LITRES),
                        ingredientInput("ingredient-3", 300.00, QuantityType.MILLILITRES)
                ));
    }

    public static IngredientInput ingredientInput(final String name, final Double quantity, final QuantityType type) {
        return IngredientInput.builder()
                .name(name)
                .quantity(quantity)
                .quantityType(type)
                .build();
    }

}
