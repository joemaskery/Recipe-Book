package org.recipes.recipe.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.recipes.recipe.model.QuantityType;

@Data
@AllArgsConstructor
public class IngredientSummary {

    private Integer ingredientRefId;
    private Double quantity;
    private QuantityType quantityType;
    private String ingredientReferenceName;
    private String ingredientReferenceCategory;

}
