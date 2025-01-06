package org.recipes.recipe.service;

import org.junit.jupiter.api.Test;
import org.recipes.recipe.dto.request.ShoppingListRequest;
import org.recipes.recipe.dto.response.ShoppingList;
import org.recipes.recipe.dto.response.ShoppingListItem;
import org.recipes.recipe.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        RecipeIngredientService.class,
        ShoppingListService.class
})
class ShoppingListServiceTest {

    @MockBean RecipeIngredientRepository recipeIngredientRepository;
    @Autowired ShoppingListService shoppingListService;

    @Test
    void buildShoppingList_builds_expected_shopping_list() {
        // given

        final IngredientSummary tomato1 = new IngredientSummary(1, 2.0, QuantityType.ITEMS, "TOMATO", "FRUIT");
        final IngredientSummary tomato2 = new IngredientSummary(1, 5.0, QuantityType.ITEMS, "TOMATO", "FRUIT");
        final IngredientSummary tomato3 = new IngredientSummary(1, 100.0, QuantityType.GRAM, "TOMATO", "FRUIT");
        final IngredientSummary cheese1 = new IngredientSummary(2, 15.0, QuantityType.GRAM, "CHEESE", "DAIRY");
        final IngredientSummary cheese2 = new IngredientSummary(2, 300.0, QuantityType.GRAM, "CHEESE", "DAIRY");

        when(recipeIngredientRepository.findAllByRecipeIdIn(List.of(12345)))
                .thenReturn(List.of(tomato1, tomato2, tomato3, cheese1, cheese2));

        // when
        final ShoppingList response = shoppingListService.buildShoppingList(new ShoppingListRequest(List.of(12345)));

        // then
        assertThat(response.getItems()).containsExactlyInAnyOrder(
                new ShoppingListItem("TOMATO", 7.0, QuantityType.ITEMS),
                new ShoppingListItem("TOMATO", 100.0, QuantityType.GRAM),
                new ShoppingListItem("CHEESE", 315.0, QuantityType.GRAM)
        );
    }

    @Test
    void buildShoppingList_does_not_throw_exception_if_no_ingredients_found() {
        // given
        final ShoppingListRequest request = new ShoppingListRequest(List.of(12345));
        when(recipeIngredientRepository.findAllByRecipeIdIn(List.of(12345))).thenReturn(List.of());
        // when, then
        assertThat(shoppingListService.buildShoppingList(request))
                .isEqualTo(new ShoppingList(List.of()));
    }
}
