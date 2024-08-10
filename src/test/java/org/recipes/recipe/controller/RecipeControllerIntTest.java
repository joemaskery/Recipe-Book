package org.recipes.recipe.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.recipe.dto.AddRecipeRequest;
import org.recipes.recipe.entity.Ingredient;
import org.recipes.recipe.entity.Recipe;
import org.recipes.recipe.repository.IngredientRepository;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.AddRecipeRequestTestBuilder.addRecipeRequest;
import static org.recipes.testutils.IngredientTestBuilder.*;
import static org.recipes.testutils.RecipeTestBuilder.tomatoPasta;

class RecipeControllerIntTest extends IntegrationTest {

    private static final Integer USER_ID_1 = 1;

    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientRepository ingredientRepository;

    @Test
    void canGetAllRecipes() {
        // given, when
        Response response = given()
                .get("/recipe/get-for-user/1");

        // then
        final Recipe[] recipes = response.getBody().as(Recipe[].class);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(recipes.length).isEqualTo(1);

        assertThat(recipes[0]).usingRecursiveComparison()
                .ignoringFields("ingredients")
                .isEqualTo(tomatoPasta(USER_ID_1).recipeId(1).build());

        assertThat(recipes[0].getIngredients())
                .containsExactlyInAnyOrder(
                        tomato(1.0).recipeId(1).ingredientId(1).build(),
                        pasta(100.0).recipeId(1).ingredientId(2).build(),
                        cheese(30.0).recipeId(1).ingredientId(3).build(),
                        garlicBread(0.5).recipeId(1).ingredientId(4).build());
    }

    @Test
    void canAddRecipe() {
        // given
        final AddRecipeRequest request = addRecipeRequest(USER_ID_1).build();
        // when
        final Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
                .post("/recipe/add");
        // then
        final Recipe recipeResponse = response.as(Recipe.class);
        final Recipe recipeEntity = recipeRepository.findById(recipeResponse.getRecipeId()).get();

        assertThat(recipeEntity).usingRecursiveComparison().ignoringFields("ingredients")
                .isEqualTo(Recipe.builder()
                        .userId(USER_ID_1)
                        .recipeId(recipeResponse.getRecipeId())
                        .name(request.getName())
                        .description(request.getDescription())
                        .weblink(request.getWeblink())
                        .build()
                );

        final List<Ingredient> ingredientEntities = ingredientRepository.findAllByRecipeId(recipeResponse.getRecipeId());

        assertThat(ingredientEntities).extracting("name")
                .containsExactlyInAnyOrder("ingredient-1", "ingredient-2", "ingredient-3");
    }

}
