package org.recipes.shopping.list.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.recipes.MongoDbIntegrationTest;
import org.recipes.commons.exception.ErrorResponse;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.UpdateShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.shopping.list.entity.ShoppingList;
import org.recipes.shopping.list.entity.ShoppingListItem;
import org.recipes.shopping.list.repository.ShoppingListRepository;
import org.recipes.user.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.recipes.testutils.UserHelper.USER_1;
import static org.recipes.testutils.UserHelper.USER_1_TOKEN;

class ShoppingListControllerIntTest extends MongoDbIntegrationTest {

    @MockBean RecipeIngredientRepository recipeIngredientRepository;
    @MockBean UserDetailsServiceImpl userDetailsService;

    @Autowired ShoppingListRepository shoppingListRepository;

    @BeforeEach
    void setUp() {
        when(userDetailsService.loadUserByUsername(USER_1.getEmail())).thenReturn(USER_1);
    }

    @Test
    void shopping_list_build_returns_expected_shopping_list() {
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

        final SavedShoppingListSummary listSummary = response.as(SavedShoppingListSummary.class);

        // then - list is saved
        final ShoppingList savedShoppingList = shoppingListRepository.findById(listSummary.getId()).get();

        assertThat(savedShoppingList.getUser()).isEqualTo(USER_1.getEmail());
        assertThat(savedShoppingList.getName()).startsWith("Shopping List ");
        assertThat(savedShoppingList.getCreatedDate()).isNotNull();

        assertThat(savedShoppingList.getItems()).containsExactlyInAnyOrder(
                new ShoppingListItem("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                new ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                new ShoppingListItem("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                new ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );

        // then - list is returned
        assertThat(listSummary.getName()).startsWith("Shopping List ");
        assertThat(listSummary.getId()).isEqualTo(savedShoppingList.getId());

        assertThat(listSummary.getItems()).containsExactlyInAnyOrder(
                new ShoppingListSummary.ShoppingListItem("Cheese", 173.45, QuantityType.GRAM, "Dairy"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Tomato", 50.0, QuantityType.GRAM, "Fruit"),
                new ShoppingListSummary.ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );
    }

    @Test
    void shopping_list_update_updates_shopping_list() {
        // given
        final ShoppingList originalList = shoppingListRepository.save(ShoppingList.builder()
                .name("Shopping list")
                .user(USER_1.getEmail())
                .items(List.of(
                        new ShoppingListItem("Cheese", 50.0, QuantityType.GRAM, "Dairy"),
                        new ShoppingListItem("Tomato", 15.0, QuantityType.ITEMS, "Fruit")))
                .build());

        final UpdateShoppingListRequest request = UpdateShoppingListRequest.builder()
                .id(originalList.getId())
                .name("New shopping list name")
                .items(List.of(
                        new UpdateShoppingListRequest.ShoppingListItem("Cheese", 50.0, QuantityType.GRAM, "Dairy"),
                        new UpdateShoppingListRequest.ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")))
                .build();

        // when
        final Response response = given()
                .header("Authorization", USER_1_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .put("/shopping-list/update");

        final SavedShoppingListSummary listSummary = response.as(SavedShoppingListSummary.class);

        // then - updated list is saved
        final ShoppingList updatedList = shoppingListRepository.findById(originalList.getId()).get();

        assertThat(updatedList.getUser()).isEqualTo(USER_1.getEmail());
        assertThat(updatedList.getName()).isEqualTo("New shopping list name");
        assertThat(updatedList.getModifiedDate()).isNotNull();

        assertThat(updatedList.getItems()).containsExactlyInAnyOrder(
                new ShoppingListItem("Cheese", 50.0, QuantityType.GRAM, "Dairy"),
                new ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );

        // then - updated list is returned
        assertThat(listSummary.getId()).isEqualTo(originalList.getId());
        assertThat(listSummary.getName()).isEqualTo("New shopping list name");

        assertThat(listSummary.getItems()).containsExactlyInAnyOrder(
                new ShoppingListSummary.ShoppingListItem("Cheese", 50.0, QuantityType.GRAM, "Dairy"),
                new ShoppingListSummary.ShoppingListItem("Flour", 100.0, QuantityType.GRAM, "Baking")
        );
    }

    @ParameterizedTest
    @MethodSource("shoppingListItemsAndExpectedErrorMessages")
    void shopping_list_update_returns_400_for_invalid_request(final List<UpdateShoppingListRequest.ShoppingListItem> items,
                                                              final String[] errorMessages) {
        // given
        final UpdateShoppingListRequest request = UpdateShoppingListRequest.builder()
                .id(" ")
                .name(" ")
                .items(items)
                .build();

        // when
        final Response response = given()
                .header("Authorization", USER_1_TOKEN)
                .body(request)
                .contentType(ContentType.JSON)
                .put("/shopping-list/update");

        final ErrorResponse errorResponse = response.getBody().as(ErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(400);
        assertThat(errorResponse.getErrors())
                .containsExactlyInAnyOrder(errorMessages);
    }

    private static Stream<Arguments> shoppingListItemsAndExpectedErrorMessages() {
        return Stream.of(
                Arguments.of(
                        List.of(),
                        new String[]{"Shopping list ID must not be blank",
                                "Shopping list name must not be blank",
                                "Shopping list must have items"}),

                Arguments.of(
                        List.of(new UpdateShoppingListRequest.ShoppingListItem(" ", null, null, " ")),
                        new String[]{"Shopping list ID must not be blank",
                                "Shopping list name must not be blank",
                                "Item name must not be blank",
                                "Item quantity must not be null",
                                "Item quantity type must not be null",
                                "Item category must not be blank"})
        );
    }
}