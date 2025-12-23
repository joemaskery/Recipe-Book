package org.recipes.shopping.list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.recipe.service.RecipeIngredientService;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.SaveShoppingListRequest;
import org.recipes.shopping.list.dto.response.SavedShoppingListSummary;
import org.recipes.shopping.list.dto.response.ShoppingListSummary;
import org.recipes.shopping.list.entity.ShoppingList;
import org.recipes.shopping.list.entity.ShoppingListItem;
import org.recipes.shopping.list.repository.ShoppingListRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingListService {

    private final RecipeIngredientService recipeIngredientService;
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListSummary buildShoppingList(final BuildShoppingListRequest request) {
        final List<IngredientSummary> ingredients =
                recipeIngredientService.getIngredientsForRecipeIds(request.getRecipeIds());

        if (CollectionUtils.isEmpty(ingredients)) {
            LOG.warn("Can't build shopping list - no ingredients found under request: {}", request);
            return ShoppingListSummary.builder()
                    .name(getShoppingListName())
                    .items(List.of())
                    .build();
        }

        LOG.debug("Retrieved {} ingredient summaries", ingredients.size());

        final Map<Integer, List<IngredientSummary>> ingredientsByReferenceId = ingredients.stream()
                .collect(Collectors.groupingBy(IngredientSummary::getIngredientRefId));

        final ShoppingListSummary shoppingList = new ShoppingListSummary(getShoppingListName());

        ingredientsByReferenceId.forEach((refId, ingredientsForRefId) -> {
            LOG.trace("Processing ingredients with refId={}: {}", refId, ingredientsForRefId);

            final Map<QuantityType, List<IngredientSummary>> ingredientByQuantityType = ingredientsForRefId.stream()
                    .collect(Collectors.groupingBy(IngredientSummary::getQuantityType));

            ingredientByQuantityType.forEach((quantityType, ingredientsForQuantityType) -> {
                LOG.trace("Processing refId={} with quantityType={}: {}", refId, quantityType, ingredientsForQuantityType);
                final Double totalQuantity = ingredientsForQuantityType.stream()
                        .map(IngredientSummary::getQuantity)
                        .mapToDouble(i -> i)
                        .sum();

                shoppingList.addItem(ingredientsForQuantityType.getFirst().getIngredientReferenceName(), totalQuantity,
                        quantityType, ingredientsForQuantityType.getFirst().getIngredientReferenceCategory());
            });

        });

        LOG.info("Calculated {} items for shopping list", shoppingList.getItems().size());
        LOG.trace("Shopping list items: {}", shoppingList.getItems());

        return shoppingList;
    }

    public SavedShoppingListSummary saveShoppingList(final SaveShoppingListRequest request) {
        LOG.trace("Saving shopping list: {}", request);
        final ShoppingList shoppingList = mapToShoppingList(request);
        final ShoppingList savedShoppingList = shoppingListRepository.save(shoppingList);
        LOG.info("Saved shopping list: Name=[{}], ID=[{}]", savedShoppingList.getName(), savedShoppingList.getId());

        return new SavedShoppingListSummary(
                savedShoppingList.getId(),
                mapToShoppingListSummary(savedShoppingList)
        );
    }

    private ShoppingList mapToShoppingList(final SaveShoppingListRequest request) {
        return ShoppingList.builder()
                .name(request.getName())
                .user(getUserEmail())
                .items(toShoppingListItems(request.getItems()))
                .build();
    }

    private List<ShoppingListItem> toShoppingListItems(
            final List<SaveShoppingListRequest.ShoppingListItemInput> items) {
        return items.stream()
                .map(item -> ShoppingListItem.builder()
                        .name(item.name())
                        .quantity(item.quantity())
                        .quantityType(item.quantityType())
                        .category(item.category())
                        .build())
                .toList();
    }

    private ShoppingListSummary mapToShoppingListSummary(final ShoppingList shoppingList) {
        final ShoppingListSummary summary = new ShoppingListSummary(shoppingList.getName());

        shoppingList.getItems().forEach(item ->
                summary.addItem(item.getName(), item.getQuantity(), item.getQuantityType(), item.getCategory())
        );

        return summary;
    }

    private String getShoppingListName() {
        final LocalDateTime now = LocalDateTime.now();
        final String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return String.format("Shopping List %s", formatted);
    }

    private String getUserEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
    }
}
