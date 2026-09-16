package org.recipes.shopping.list.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.recipes.commons.exception.NotFoundException;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.recipe.service.RecipeIngredientService;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.UpdateShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.shopping.list.entity.ShoppingList;
import org.recipes.shopping.list.repository.ShoppingListRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.recipes.testutils.UserHelper.USER_1;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock RecipeIngredientRepository recipeIngredientRepository;
    @Mock ShoppingListRepository shoppingListRepository;

    ShoppingListService shoppingListService;

    @BeforeAll
    static void setupSecurityContext() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(USER_1);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    void setUp() {
        shoppingListService = new ShoppingListService(
                new RecipeIngredientService(recipeIngredientRepository),
                shoppingListRepository
        );
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

        when(shoppingListRepository.save(any())).thenAnswer(returnsFirstArg());

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

        when(shoppingListRepository.save(any())).thenAnswer(returnsFirstArg());

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

    @Test
    void getShoppingListById_throws_exception_if_shopping_list_is_not_found() {
        assertThatThrownBy(() -> shoppingListService.getShoppingListById("nonexistent-shopping-list"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No shopping list found with ID: nonexistent-shopping-list");
    }

    @ParameterizedTest
    @MethodSource("shoppingListDates")
    void getByUserToken_maps_expected_shopping_list_date(final LocalDateTime createdDate,
                                                         final LocalDateTime modifiedDate,
                                                         final LocalDate expectedDate) {
        // given
        final String email = USER_1.getEmail();
        final ShoppingList shoppingList = Mockito.mock(ShoppingList.class);

        lenient().when(shoppingList.getCreatedDate()).thenReturn(createdDate);
        lenient().when(shoppingList.getModifiedDate()).thenReturn(modifiedDate);

        when(shoppingListRepository.findByUser(email)).thenReturn(List.of(shoppingList));

        // when, then
        assertThat(shoppingListService.getByUser())
                .extracting(SavedShoppingListSummary::getDate)
                .containsExactly(expectedDate);
    }

    @Test
    void updateShoppingList_throws_exception_if_shopping_list_is_not_found() {
        // given
        final UpdateShoppingListRequest request = UpdateShoppingListRequest.builder()
                .id("nonexistent-shopping-list")
                .build();

        // when, then
        assertThatThrownBy(() -> shoppingListService.updateShoppingList(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Can't update shopping list - no list found with ID: nonexistent-shopping-list");
    }

    public static Stream<Arguments> shoppingListDates() {
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(LocalDateTime.of(2025, 1, 1, 12, 0, 0), null, LocalDate.of(2025, 1, 1)),
                Arguments.of(LocalDateTime.of(2025, 1, 1, 12, 0, 0), LocalDateTime.of(2026, 2, 2, 15, 0, 0),
                        LocalDate.of(2026, 2, 2))
        );
    }
}
