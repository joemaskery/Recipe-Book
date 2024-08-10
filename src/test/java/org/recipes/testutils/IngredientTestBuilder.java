package org.recipes.testutils;

import org.recipes.recipe.entity.Ingredient;
import org.recipes.recipe.entity.Ingredient.IngredientBuilder;
import org.recipes.recipe.model.QuantityType;

public class IngredientTestBuilder {

    public static IngredientBuilder tomato(final Double quantity) {
        return Ingredient.builder()
                .name("Tomato")
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS);
    }

    public static IngredientBuilder pasta(final Double quantity) {
        return Ingredient.builder()
                .name("Fusilli Pasta")
                .quantity(quantity)
                .quantityType(QuantityType.GRAM);
    }

    public static IngredientBuilder cheese(final Double quantity) {
        return Ingredient.builder()
                .name("Cheddar Cheese")
                .quantity(quantity)
                .quantityType(QuantityType.GRAM);
    }

    public static IngredientBuilder garlicBread(final Double quantity) {
        return Ingredient.builder()
                .name("Garlic Bread")
                .quantity(quantity)
                .quantityType(QuantityType.ITEMS);
    }

    public static IngredientBuilder pizzaDough(final Double quantity) {
        return Ingredient.builder()
                .name("Pizza Dough")
                .quantity(quantity)
                .quantityType(QuantityType.GRAM);
    }

}
