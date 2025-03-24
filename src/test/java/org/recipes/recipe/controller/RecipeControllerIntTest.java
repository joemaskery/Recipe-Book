package org.recipes.recipe.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.auth.security.JwtHelper;
import org.recipes.recipe.dto.request.AddRecipeRequest;
import org.recipes.recipe.dto.response.UserRecipe;
import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.entity.RecipeIngredientEntity;
import org.recipes.recipe.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.RecipeRepository;
import org.recipes.testutils.IngredientHelper;
import org.recipes.testutils.RecipeHelper;
import org.recipes.testutils.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.builder.AddRecipeRequestTestBuilder.addRecipeRequest;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.cheese;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.garlicBread;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.pasta;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.tomato;
import static org.recipes.testutils.builder.RecipeTestBuilder.tomatoPastaRecipe;

@ActiveProfiles("test")
class RecipeControllerIntTest extends IntegrationTest {

    private static final Integer USER_ID_1 = 1;
    private static final Integer RECIPE_ID_1 = 1;
    private static final String USER_1_EMAIL = "email1@domain.com";

    @Autowired RecipeRepository recipeRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;
    @Autowired RecipeHelper recipeHelper;
    @Autowired IngredientHelper ingredientHelper;
    @Autowired UserHelper userHelper;

    @Test
    void getUserRecipesByUserId_returns_all_user_recipes() {
        // given
        recipeHelper.saveRecipes();

        // when
        Response response = given().get("/recipe/get-for-user/1");
        final UserRecipe[] recipes = response.getBody().as(UserRecipe[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(recipes.length).isEqualTo(1);

        assertThat(recipes[0]).usingRecursiveComparison()
                .ignoringFields("ingredients")
                .isEqualTo(tomatoPastaRecipe(RECIPE_ID_1, USER_ID_1).build());

        assertThat(recipes[0].getIngredients()).containsExactlyInAnyOrder(
                tomato(1.0).build(),
                pasta(100.0).build(),
                cheese(30.0).build(),
                garlicBread(0.5).build()
        );
    }

    @Test
    void getUserRecipes_returns_all_user_recipes() {
        // given
        recipeHelper.saveRecipes();
        final String token = String.format("Bearer %s", JwtHelper.generateToken(USER_1_EMAIL));

        // when
        Response response = given()
                .header("Authorization", token)
                .get("/recipe/get-for-user");
        final UserRecipe[] recipes = response.getBody().as(UserRecipe[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(recipes.length).isEqualTo(1);

        assertThat(recipes[0]).usingRecursiveComparison()
                .ignoringFields("ingredients")
                .isEqualTo(tomatoPastaRecipe(RECIPE_ID_1, USER_ID_1).build());

        assertThat(recipes[0].getIngredients()).containsExactlyInAnyOrder(
                tomato(1.0).build(),
                pasta(100.0).build(),
                cheese(30.0).build(),
                garlicBread(0.5).build()
        );
    }

    @Test
    void getRecipeById_returns_recipe() {
        // given
        recipeHelper.saveRecipes();

        // when
        Response response = given()
                .get("/recipe/1");
        final UserRecipe recipe = response.getBody().as(UserRecipe.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(recipe).usingRecursiveComparison()
                .ignoringFields("ingredients")
                .isEqualTo(tomatoPastaRecipe(RECIPE_ID_1, USER_ID_1).build());

        assertThat(recipe.getIngredients()).containsExactlyInAnyOrder(
                tomato(1.0).build(),
                pasta(100.0).build(),
                cheese(30.0).build(),
                garlicBread(0.5).build()
        );
    }

    @Test
    void addRecipe_adds_recipe() {
        // given
        userHelper.saveUsers();
        ingredientHelper.saveIngredients();
        final String token = String.format("Bearer %s", JwtHelper.generateToken(USER_1_EMAIL));

        final AddRecipeRequest request = addRecipeRequest().build();
        // when
        final Response response = given()
                .header("Authorization", token)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/recipe/add");
        final UserRecipe recipeResponse = response.as(UserRecipe.class);
        // then
        final RecipeEntity recipeEntity = recipeRepository.findById(recipeResponse.getRecipeId()).get();

        assertThat(recipeEntity).usingRecursiveComparison().ignoringFields("recipeIngredients")
                .isEqualTo(RecipeEntity.builder()
                        .userId(USER_ID_1)
                        .recipeId(recipeResponse.getRecipeId())
                        .name(request.getName())
                        .description(request.getDescription())
                        .weblink(request.getWeblink())
                        .build()
                );

        final List<RecipeIngredientEntity> recipeIngredientEntities = recipeIngredientRepository.findAllByRecipeId(recipeResponse.getRecipeId());

        assertThat(recipeIngredientEntities).extracting("name", "quantity", "quantityType")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Tomato", 100.0, QuantityType.GRAM),
                        Tuple.tuple("Fusilli Pasta", 200.0, QuantityType.LITRES),
                        Tuple.tuple("Cheddar Cheese", 300.0, QuantityType.MILLILITRES));
    }

}
