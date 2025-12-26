package org.recipes.testutils.builder;

import org.recipes.commons.model.QuantityType;
import org.recipes.shopping.list.dto.request.SaveShoppingListRequest;

public class ShoppingListItemInputTestBuilder {

    public static SaveShoppingListRequest.ShoppingListItemInput shoppingListItemInput(final String name,
                                                                                      final Double quantity,
                                                                                      final QuantityType quantityType,
                                                                                      final String category) {
        return new SaveShoppingListRequest.ShoppingListItemInput(name, quantity, quantityType, category);
    }
}
