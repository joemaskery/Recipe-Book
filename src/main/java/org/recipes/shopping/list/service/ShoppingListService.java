package org.recipes.shopping.list.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.recipes.commons.exception.NotFoundException;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.recipe.service.RecipeIngredientService;
import org.recipes.shopping.list.dto.request.BuildShoppingListRequest;
import org.recipes.shopping.list.dto.request.UpdateShoppingListRequest;
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

    @Transactional
    public SavedShoppingListSummary buildAndSaveShoppingList(final BuildShoppingListRequest request) {
        final List<IngredientSummary> ingredients =
                recipeIngredientService.getIngredientsForRecipeIds(request.getRecipeIds());

        if (CollectionUtils.isEmpty(ingredients)) {
            LOG.warn("Can't build shopping list - no ingredients found under request: {}", request);
            throw new NotFoundException("Can't build shopping list - no ingredients were found");
        }

        LOG.debug("Retrieved {} ingredient summaries", ingredients.size());

        final ShoppingListSummary shoppingListSummary = generateShoppingListSummary(ingredients);

        final ShoppingList savedShoppingList = saveShoppingList(shoppingListSummary);

        return new SavedShoppingListSummary(
                savedShoppingList.getId(),
                mapToShoppingListSummary(savedShoppingList)
        );
    }

    @Transactional
    public SavedShoppingListSummary updateShoppingList(final UpdateShoppingListRequest request) {
        final ShoppingList shoppingList = shoppingListRepository.findById(request.getId())
                .orElseThrow(() -> {
                    LOG.error("Can't update shopping list - No Shopping List found with ID: {}", request.getId());
                    return new NotFoundException("Can't update shopping list - no list found with ID: " + request.getId());
                });
        LOG.trace("Shopping list to be updated: {}", shoppingList);

        shoppingList.setName(request.getName());
        shoppingList.setItems(mapUpdateShoppingListItems(request.getItems()));

        shoppingListRepository.save(shoppingList);

        LOG.info("Successfully updated shopping list: {}", shoppingList.getId());
        return new SavedShoppingListSummary(
                shoppingList.getId(),
                mapToShoppingListSummary(shoppingList)
        );
    }

    private ShoppingListSummary generateShoppingListSummary(final List<IngredientSummary> ingredients) {
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

    private ShoppingList saveShoppingList(final ShoppingListSummary shoppingListSummary) {
        LOG.trace("Saving shopping list: {}", shoppingListSummary);
        final ShoppingList shoppingList = mapToShoppingList(shoppingListSummary);
        final ShoppingList savedShoppingList = shoppingListRepository.save(shoppingList);
        LOG.info("Saved shopping list: Name=[{}], ID=[{}]", savedShoppingList.getName(), savedShoppingList.getId());
        return savedShoppingList;
    }

    private ShoppingList mapToShoppingList(final ShoppingListSummary summary) {
        return ShoppingList.builder()
                .name(summary.getName())
                .user(getUserEmail())
                .items(mapShoppingListSummaryItems(summary.getItems()))
                .build();
    }

    private List<ShoppingListItem> mapShoppingListSummaryItems(final List<ShoppingListSummary.ShoppingListItem> items) {
        return items.stream()
                .map(item -> ShoppingListItem.builder()
                        .name(item.name())
                        .quantity(item.quantity())
                        .quantityType(item.quantityType())
                        .category(item.category())
                        .build())
                .toList();
    }

    private List<ShoppingListItem> mapUpdateShoppingListItems(final List<UpdateShoppingListRequest.ShoppingListItem> items) {
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
