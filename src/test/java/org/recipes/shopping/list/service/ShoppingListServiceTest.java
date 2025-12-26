package org.recipes.shopping.list.service;

import org.junit.jupiter.api.Test;
import org.recipes.recipe.service.RecipeIngredientService;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.shopping.list.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        RecipeIngredientService.class,
        ShoppingListService.class
})
class ShoppingListServiceTest {

    @MockBean RecipeIngredientRepository recipeIngredientRepository;
    @MockBean ShoppingListRepository shoppingListRepository;
    @Autowired ShoppingListService shoppingListService;

    @Test
    void buildShoppingList_returns_temporary_shopping_list_name() {
        // given
        Pattern expectedStringPattern = Pattern.compile(
                "Shopping List \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"
        );
        // when
        final ShoppingListSummary result
                = shoppingListService.buildShoppingList(new BuildShoppingListRequest(List.of(12345)));
        // then
        assertThat(expectedStringPattern.matcher(result.getName()).matches())
                .isTrue();
    }

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
        final ShoppingListSummary response =
                shoppingListService.buildShoppingList(new BuildShoppingListRequest(List.of(12345)));

        // then
        assertThat(response.getItems()).containsExactlyInAnyOrder(
                new ShoppingListSummary.ShoppingListItem("TOMATO", 7.0, QuantityType.ITEMS, "FRUIT"),
                new ShoppingListSummary.ShoppingListItem("TOMATO", 100.0, QuantityType.GRAM, "FRUIT"),
                new ShoppingListSummary.ShoppingListItem("CHEESE", 315.0, QuantityType.GRAM, "DAIRY")
        );
    }

    @Test
    void buildShoppingList_does_not_throw_exception_if_no_ingredients_found() {
        // given
        final BuildShoppingListRequest request = new BuildShoppingListRequest(List.of(12345));
        when(recipeIngredientRepository.findAllByRecipeIdIn(List.of(12345))).thenReturn(List.of());
        // when
        final ShoppingListSummary result = shoppingListService.buildShoppingList(request);
        // then
        assertThat(result.getName()).isNotNull();

        assertThat(result)
                .extracting(ShoppingListSummary::getItems)
                .isEqualTo(List.of());
    }
}
