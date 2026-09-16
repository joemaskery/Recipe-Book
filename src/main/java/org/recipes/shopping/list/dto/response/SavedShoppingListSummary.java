package org.recipes.shopping.list.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SavedShoppingListSummary extends ShoppingListSummary {

    private String id;
    private LocalDate date;

    public SavedShoppingListSummary(final String id,
                                    final LocalDate date,
                                    final ShoppingListSummary shoppingListSummary) {
        super(shoppingListSummary.getName(), shoppingListSummary.getItems());
        this.id = id;
        this.date = date;
    }
}

