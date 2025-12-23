package org.recipes.shopping.list.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SavedShoppingListSummary extends ShoppingListSummary {

    private String id;

    public SavedShoppingListSummary(final String id, final ShoppingListSummary shoppingListSummary) {
        super(shoppingListSummary.getName(), shoppingListSummary.getItems());
        this.id = id;
    }
}

