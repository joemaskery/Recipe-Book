package org.recipes.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.commons.model.KeyValue;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReferenceIngredientsResponse {

    List<KeyValue> quantityTypes;
    List<ReferenceIngredient> referenceIngredients;
}
