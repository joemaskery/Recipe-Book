package org.recipes.shopping.list.entity;

import lombok.Builder;
import lombok.Data;
import org.recipes.commons.model.QuantityType;

@Data
@Builder
public class ShoppingListItem {

    private String name;
    private String category;
    private Double quantity;
    private QuantityType quantityType;
}
