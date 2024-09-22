package org.recipes.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.model.QuantityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredient {

    private String name;
    private String category;
    private Double quantity;
    private QuantityType quantityType;

}
