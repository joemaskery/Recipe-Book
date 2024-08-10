package org.recipes.recipe.dto;

import lombok.Builder;
import lombok.Data;
import org.recipes.recipe.model.QuantityType;

@Data
@Builder
public class IngredientInput {

    private String name;
    private Double quantity;
    private QuantityType quantityType;

}
