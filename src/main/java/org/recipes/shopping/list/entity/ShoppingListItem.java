package org.recipes.shopping.list.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.recipes.commons.model.QuantityType;

@Data
@Builder
@AllArgsConstructor
public class ShoppingListItem {

    private String name;
    private Double quantity;
    private QuantityType quantityType;
    private String category;
}
