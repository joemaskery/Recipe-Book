package org.recipes.shopping.list.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.commons.model.QuantityType;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveShoppingListRequest {

    private String name;
    private List<ShoppingListItemInput> items;

    public record ShoppingListItemInput(String name, Double quantity, QuantityType quantityType, String category) {}
}
