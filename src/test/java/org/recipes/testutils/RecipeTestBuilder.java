package org.recipes.testutils;

import org.recipes.recipe.entity.Recipe;
import org.recipes.recipe.entity.Recipe.RecipeBuilder;

import java.util.List;

import static org.recipes.testutils.IngredientTestBuilder.*;
import static org.recipes.testutils.IngredientTestBuilder.garlicBread;

public class RecipeTestBuilder {

    public static RecipeBuilder tomatoPasta(final Integer userId) {
        return Recipe.builder()
                .userId(userId)
                .name("Tomato Pasta")
                .description("GOATED tomato pasta recipe")
                .ingredients(List.of(
                        tomato(1.0).build(),
                        pasta(100.0).build(),
                        cheese(30.0).build(),
                        garlicBread(0.5).build()
                ))
                .weblink("www.recipes-here.com");
    }

    public static RecipeBuilder pizza(final Integer userId) {
        return Recipe.builder()
                .userId(userId)
                .name("Margherita Pizza")
                .description("GOATED margherita pizza recipe")
                .ingredients(List.of(
                        tomato(5.0).build(),
                        pizzaDough(300.0).build(),
                        cheese(200.0).build()
                ))
                .weblink("www.recipes-here.com");
    }

}
