package org.recipes.recipe.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.MariaDbIntegrationTest;
import org.recipes.auth.security.JwtHelper;
import org.recipes.commons.model.KeyValue;
import org.recipes.recipe.dto.request.AddIngredientRequest;
import org.recipes.recipe.dto.response.ReferenceIngredient;
import org.recipes.recipe.dto.response.ReferenceIngredientsResponse;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.recipes.testutils.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.UserHelper.USER_1;
import static org.recipes.testutils.UserHelper.USER_2;

@ActiveProfiles("test")
class IngredientReferenceControllerIntTest extends MariaDbIntegrationTest {

    @Autowired IngredientReferenceRepository ingredientRepository;
    @Autowired UserHelper userHelper;

    @Test
    void getUserIngredients_returns_expected_ingredients() {
        // given
        userHelper.saveUsers();
        ingredientRepository.saveAll(List.of(
                IngredientEntity.builder().name("Egg").category("Protein").userId(USER_1.getUserId()).allUsers(false).build(),
                IngredientEntity.builder().name("Cheese").category("Dairy").userId(USER_2.getUserId()).allUsers(false).build(),
                IngredientEntity.builder().name("Pepper").category("Vegetable").userId(null).allUsers(true).build()
        ));

        final String token = JwtHelper.generateTokenWithBearerPrefix(USER_1.getEmail());

        // when
        Response response = given()
                .header("Authorization", token)
                .get("/ingredient/get-for-user");
        final ReferenceIngredientsResponse recipes = response.getBody().as(ReferenceIngredientsResponse.class);

        // then
        assertThat(recipes.getQuantityTypes()).containsExactlyInAnyOrder(
                new KeyValue(QuantityType.ITEMS.name(), "items"),
                new KeyValue(QuantityType.GRAM.name(), "grams"),
                new KeyValue(QuantityType.KILOGRAM.name(), "kilograms"),
                new KeyValue(QuantityType.MILLILITRES.name(), "millilitres"),
                new KeyValue(QuantityType.LITRES.name(), "litres"),
                new KeyValue(QuantityType.TSP.name(), "tsp"),
                new KeyValue(QuantityType.TBSP.name(), "tbsp")
        );

        assertThat(recipes.getReferenceIngredients()).containsExactlyInAnyOrder(
                new ReferenceIngredient(1, "Egg", "Protein", false),
                new ReferenceIngredient(3, "Pepper", "Vegetable", true)
        );
    }

    @Test
    void addIngredient_adds_ingredient() {
        // given
        userHelper.saveUsers();
        final AddIngredientRequest request = new AddIngredientRequest("Apple", "Fruit", 2);
        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
                .post("/ingredient/add");
        final ReferenceIngredient ingredientResponse = response.getBody().as(ReferenceIngredient.class);
        // then
        assertThat(ingredientRepository.existsById(1)).isTrue();
        assertThat(ingredientResponse).isEqualTo(new ReferenceIngredient(1, "Apple", "Fruit", false));
    }

}
