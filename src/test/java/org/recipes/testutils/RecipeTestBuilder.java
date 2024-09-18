package org.recipes.testutils;

import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.entity.RecipeEntity.RecipeEntityBuilder;

public class RecipeTestBuilder {

    public static RecipeEntityBuilder tomatoPasta(final Integer userId) {
        return RecipeEntity.builder()
                .userId(userId)
                .name("Tomato Pasta")
                .description("GOATED tomato pasta recipe")
                .weblink("www.recipes-here.com");
    }

    public static RecipeEntityBuilder pizza(final Integer userId) {
        return RecipeEntity.builder()
                .userId(userId)
                .name("Margherita Pizza")
                .description("GOATED margherita pizza recipe")
                .weblink("www.recipes-here.com");
    }

}
