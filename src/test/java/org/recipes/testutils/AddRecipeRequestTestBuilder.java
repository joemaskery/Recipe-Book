package org.recipes.testutils;

import org.recipes.recipe.dto.request.AddRecipeRequest;
import org.recipes.recipe.dto.request.AddRecipeRequest.AddRecipeRequestBuilder;
import org.recipes.recipe.dto.request.IngredientInput;
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
                        ingredientInput(1, 100.00, QuantityType.GRAM),
                        ingredientInput(2, 200.00, QuantityType.LITRES),
                        ingredientInput(3, 300.00, QuantityType.MILLILITRES)
                ));
    }

    public static IngredientInput ingredientInput(final Integer refId, final Double quantity, final QuantityType type) {
        return IngredientInput.builder()
                .ingredientRefId(refId)
                .quantity(quantity)
                .quantityType(type)
                .build();
    }

}
