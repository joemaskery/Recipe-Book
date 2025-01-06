package org.recipes.recipe.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.recipe.dto.request.ShoppingListRequest;
import org.recipes.recipe.dto.response.ShoppingList;
import org.recipes.recipe.dto.response.ShoppingListItem;
import org.recipes.recipe.model.QuantityType;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ShoppingListService {

    private final RecipeIngredientService recipeIngredientService;

    public ShoppingList buildShoppingList(final ShoppingListRequest request) {
        final List<IngredientSummary> ingredients =
                recipeIngredientService.getIngredientsForRecipeIds(request.getRecipeIds());

        if (CollectionUtils.isEmpty(ingredients)) {
            LOG.warn("Can't build shopping list - no ingredients found under request: {}", request);
            return ShoppingList.builder()
                    .items(List.of())
                    .build();
        }

        LOG.debug("Retrieved {} ingredient summaries", ingredients.size());

        final Map<Integer, List<IngredientSummary>> ingredientsByReferenceId = ingredients.stream()
                .collect(Collectors.groupingBy(IngredientSummary::getIngredientRefId));

        final List<ShoppingListItem> items = new ArrayList<>();

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

                items.add(new ShoppingListItem(ingredientsForQuantityType.getFirst().getIngredientReferenceName(),
                        totalQuantity, quantityType));
            });

        });

        LOG.info("Calculated {} items for shopping list", items.size());
        LOG.trace("Shopping list items: {}", items);

        return new ShoppingList(items);
    }

}
