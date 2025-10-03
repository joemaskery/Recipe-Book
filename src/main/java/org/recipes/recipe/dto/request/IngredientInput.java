package org.recipes.recipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.model.QuantityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientInput {

    @NotNull(message = "Ingredient reference ID must be defined")
    private Integer ingredientRefId;

    @NotNull(message = "Ingredient quantity must be defined")
    private Double quantity;

    @NotNull(message = "Ingredient quantity type must be defined")
    private QuantityType quantityType;
}
