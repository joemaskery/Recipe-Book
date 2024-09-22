package org.recipes.testutils;

import org.recipes.recipe.entity.IngredientEntity;

public class IngredientTestBuilder {

    public static IngredientEntity tomatoReference() {
        return IngredientEntity.builder()
                .name("Tomato")
                .category("Fruit")
                .allUsers(true)
                .build();
    }

    public static IngredientEntity pastaReference() {
        return IngredientEntity.builder()
                .name("Fusilli Pasta")
                .category("Baking & Grains")
                .allUsers(true)
                .build();
    }

    public static IngredientEntity cheddarCheeseReference() {
        return IngredientEntity.builder()
                .name("Cheddar Cheese")
                .category("Dairy")
                .allUsers(true)
                .build();
    }

    public static IngredientEntity garlicBreadReference() {
        return IngredientEntity.builder()
                .name("Garlic Bread")
                .category("Baking & Grains")
                .allUsers(true)
                .build();
    }

    public static IngredientEntity pizzaDoughReference() {
        return IngredientEntity.builder()
                .name("Pizza Dough")
                .category("Baking & Grains")
                .allUsers(true)
                .build();
    }

}
