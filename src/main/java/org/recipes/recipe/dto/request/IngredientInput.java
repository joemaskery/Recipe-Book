package org.recipes.recipe.dto.request;

import lombok.Builder;
import lombok.Data;
import org.recipes.recipe.model.QuantityType;

@Data
@Builder
public class IngredientInput {

    private Integer ingredientRefId;
    private Double quantity;
    private QuantityType quantityType;

}
