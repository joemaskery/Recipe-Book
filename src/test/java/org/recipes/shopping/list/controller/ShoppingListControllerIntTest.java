package org.recipes.shopping.list.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.recipes.MongoDbIntegrationTest;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.SaveShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.shopping.list.entity.ShoppingList;
import org.recipes.shopping.list.entity.ShoppingListItem;
import org.recipes.shopping.list.repository.ShoppingListRepository;
import org.recipes.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.recipes.testutils.UserHelper.USER_1;
import static org.recipes.testutils.UserHelper.USER_1_TOKEN;
import static org.recipes.testutils.builder.ShoppingListItemInputTestBuilder.shoppingListItemInput;

class ShoppingListControllerIntTest extends MongoDbIntegrationTest {

    @MockBean RecipeIngredientRepository recipeIngredientRepository;
    @MockBean UserDetailsServiceImpl userDetailsService;

    @Autowired ShoppingListRepository shoppingListRepository;

    @BeforeEach
    void setUp() {
        when(userDetailsService.loadUserByUsername(USER_1.getEmail())).thenReturn(USER_1);
    }

    @Test
    void buildShoppingList_builds_shopping_list() {
        // given
        final BuildShoppingListRequest request = new BuildShoppingListRequest(List.of(1, 2));

        when(recipeIngredientRepository.findAllByRecipeIdIn(request.getRecipeIds()))
                .thenReturn(List.of(
                        new IngredientSummary(1, 50.0, QuantityType.GRAM, "Cheese", "Dairy"),
                        new IngredientSummary(1, 123.45, QuantityType.GRAM, "Cheese", "Dairy"),
                        new IngredientSummary(2, 15.0, QuantityType.ITEMS, "Tomato", "Fruit"),
                        new IngredientSummary(2, 50.0, QuantityType.GRAM, "Tomato", "Fruit"),
                        new IngredientSummary(3, 100.0, QuantityType.GRAM, "Flour", "Baking")
                ));

        // when
        final Response response = given()
                .header("Authorization", USER_1_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/shopping-list/build");

        final ShoppingListSummary listSummary = response.as(ShoppingListSummary.class);

        // then
        assertThat(listSummary.getName()).isNotNull();

        assertThat(listSummary.getItems()).containsExactlyInAnyOrder(
                new ShoppingListSummary.ShoppingListItem("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );
    }

    @Test
    void saveShoppingList_saves_shopping_list_for_user() {
        // given
        final SaveShoppingListRequest request = SaveShoppingListRequest.builder()
                .name("this is a shopping list")
                .items(List.of(
                        shoppingListItemInput("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                        shoppingListItemInput("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                        shoppingListItemInput("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                        shoppingListItemInput("Flour", 100.0, QuantityType.GRAM, "Baking")
                )).build();
        // when
        final Response response = given()
                .header("Authorization", USER_1_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .post("/shopping-list/save");

        final SavedShoppingListSummary listSummary = response.as(SavedShoppingListSummary.class);

        // then
        assertThat(listSummary.getName()).isEqualTo("this is a shopping list");

        assertThat(listSummary.getItems()).containsExactly(
                new ShoppingListSummary.ShoppingListItem("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );

        final ShoppingList savedShoppingList = shoppingListRepository.findById(listSummary.getId()).get();

        assertThat(savedShoppingList)
                .extracting(ShoppingList::getName, ShoppingList::getUser)
                .isEqualTo(List.of("this is a shopping list", USER_1.getEmail()));

        assertThat(savedShoppingList.getCreatedDate()).isNotNull();

        assertThat(savedShoppingList.getItems()).containsExactlyInAnyOrder(
                new ShoppingListItem("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                new ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                new ShoppingListItem("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                new ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );
    }
}