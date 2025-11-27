package org.recipes.testutils.builder;

import org.recipes.recipe.dto.response.RecipeIngredient;
import org.recipes.recipe.dto.response.RecipeIngredient.RecipeIngredientBuilder;
import org.recipes.recipe.entity.RecipeIngredientEntity;
import org.recipes.commons.model.QuantityType;

public class RecipeIngredientTestBuilder {

    public static RecipeIngredientEntity tomatoEntity(final Integer recipeId, final Integer ingredientRefId,
                                                      final Double quantity) {
        return RecipeIngredientEntity.builder()
                .recipeId(recipeId)
                .ingredientRefId(ingredientRefId)
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS)
                .build();
    }

    public static RecipeIngredientBuilder tomato(final Double quantity) {
        return RecipeIngredient.builder()
                .name("Tomato")
                .category("Fruit")
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS)
                .quantityName(QuantityType.ITEMS.getName());
    }

    public static RecipeIngredientEntity pastaEntity(final Integer recipeId, final Integer ingredientRefId,
                                                     final Double quantity) {
        return RecipeIngredientEntity.builder()
                .recipeId(recipeId)
                .ingredientRefId(ingredientRefId)
                .quantity(quantity)
                .quantityType(QuantityType.GRAM)
                .build();
    }

    public static RecipeIngredientBuilder pasta(final Double quantity) {
        return RecipeIngredient.builder()
                .name("Fusilli Pasta")
                .category("Baking & Grains")
                .quantity(quantity)
                .quantityType(QuantityType.GRAM)
                .quantityName((QuantityType.GRAM.getName()));
    }

    public static RecipeIngredientEntity cheeseEntity(final Integer recipeId, final Integer ingredientRefId,
                                                      final Double quantity) {
        return RecipeIngredientEntity.builder()
                .recipeId(recipeId)
                .ingredientRefId(ingredientRefId)
                .quantity(quantity)
                .quantityType(QuantityType.GRAM)
                .build();
    }

    public static RecipeIngredientBuilder cheese(final Double quantity) {
        return RecipeIngredient.builder()
                .name("Cheddar Cheese")
                .category("Dairy")
                .quantity(quantity)
                .quantityType(QuantityType.GRAM)
                .quantityName(QuantityType.GRAM.getName());
    }

    public static RecipeIngredientEntity garlicBreadEntity(final Integer recipeId, final Integer ingredientRefId,
                                                           final Double quantity) {
        return RecipeIngredientEntity.builder()
                .recipeId(recipeId)
                .ingredientRefId(ingredientRefId)
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS)
                .build();
    }

    public static RecipeIngredientBuilder garlicBread(final Double quantity) {
        return RecipeIngredient.builder()
                .name("Garlic Bread")
                .category("Baking & Grains")
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS)
                .quantityName(QuantityType.ITEMS.getName());
    }

    public static RecipeIngredientEntity pizzaDoughEntity(final Integer recipeId, final Integer ingredientRefId,
                                                          final Double quantity) {
        return RecipeIngredientEntity.builder()
                .recipeId(recipeId)
                .ingredientRefId(ingredientRefId)
                .quantity(quantity)
                .quantityType(QuantityType.GRAM)
                .build();
    }

}
