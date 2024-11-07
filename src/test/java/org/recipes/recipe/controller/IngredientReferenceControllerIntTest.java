package org.recipes.recipe.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.recipe.dto.request.AddIngredientRequest;
import org.recipes.recipe.dto.response.ReferenceIngredient;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.recipes.testutils.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class IngredientReferenceControllerIntTest extends IntegrationTest {

    @Autowired IngredientReferenceRepository ingredientRepository;
    @Autowired UserHelper userHelper;

    @Test
    void getUserIngredients_returns_expected_ingredients() {
        // given
        userHelper.saveUsers();
        ingredientRepository.saveAll(List.of(
                IngredientEntity.builder().name("Egg").category("Protein").userId(1).allUsers(false).build(),
                IngredientEntity.builder().name("Cheese").category("Dairy").userId(2).allUsers(false).build(),
                IngredientEntity.builder().name("Pepper").category("Vegetable").userId(null).allUsers(true).build()
        ));
        // when
        Response response = given().get("/ingredient/get-for-user/1");
        final ReferenceIngredient[] recipes = response.getBody().as(ReferenceIngredient[].class);
        // then
        assertThat(recipes).containsExactlyInAnyOrder(
                ReferenceIngredient.builder().name("Egg").category("Protein").allUsers(false).build(),
                ReferenceIngredient.builder().name("Pepper").category("Vegetable").allUsers(true).build()
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
        assertThat(ingredientResponse).isEqualTo(new ReferenceIngredient("Apple", "Fruit", false));
    }

}
