package org.recipes.recipe.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.model.QuantityType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListItem {

    private String name;
    private Double quantity;
    private QuantityType quantityType;

}
