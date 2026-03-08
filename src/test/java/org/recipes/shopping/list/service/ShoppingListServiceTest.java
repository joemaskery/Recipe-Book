package org.recipes.shopping.list.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.recipes.commons.exception.NotFoundException;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.recipes.testutils.UserHelper.USER_1;

@SpringBootTest
class ShoppingListServiceTest {

    @MockBean RecipeIngredientRepository recipeIngredientRepository;
    @Autowired ShoppingListService shoppingListService;

    @BeforeEach
    void setupSecurityContext() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(USER_1);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void buildAndSaveShoppingList_returns_temporary_shopping_list_name() {
        // given
        final Pattern expectedStringPattern = Pattern.compile(
                "Shopping List \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"
        );

        when(recipeIngredientRepository.findAllByRecipeIdIn(List.of(12345))).thenReturn(List.of(
                new IngredientSummary(1, 2.0, QuantityType.ITEMS, "TOMATO", "FRUIT")
        ));

        // when
        final ShoppingListSummary result
                = shoppingListService.buildAndSaveShoppingList(new BuildShoppingListRequest(List.of(12345)));
        // then
        assertThat(expectedStringPattern.matcher(result.getName()).matches())
                .isTrue();
    }

    @Test
    void buildAndSaveShoppingList_correctly_merges_ingredients() {
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
                shoppingListService.buildAndSaveShoppingList(new BuildShoppingListRequest(List.of(12345)));

        // then
        assertThat(response.getItems()).containsExactlyInAnyOrder(
                new ShoppingListSummary.ShoppingListItem("TOMATO", 7.0, QuantityType.ITEMS, "FRUIT"),
                new ShoppingListSummary.ShoppingListItem("TOMATO", 100.0, QuantityType.GRAM, "FRUIT"),
                new ShoppingListSummary.ShoppingListItem("CHEESE", 315.0, QuantityType.GRAM, "DAIRY")
        );
    }

    @Test
    void buildAndSaveShoppingList_throws_exception_if_no_ingredients_are_found() {
        // given
        final BuildShoppingListRequest request = new BuildShoppingListRequest(List.of(12345));

        when(recipeIngredientRepository.findAllByRecipeIdIn(List.of(12345))).thenReturn(List.of());

        // when, then
        assertThatThrownBy(() -> shoppingListService.buildAndSaveShoppingList(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Can't build shopping list - no ingredients were found");
    }
}
