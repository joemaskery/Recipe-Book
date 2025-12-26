package org.recipes.shopping.list.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.commons.model.QuantityType;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListSummary {

    private String name;
    private List<ShoppingListItem> items;

    public ShoppingListSummary(final String name) {
        this.name = name;
    }

    public void addItem(final String name, final Double quantity, final QuantityType quantityType,
                        final String category) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        items.add(new ShoppingListItem(name, quantity, quantityType, category));
    }

    public record ShoppingListItem(String name, Double quantity, QuantityType quantityType, String category) {}
}
