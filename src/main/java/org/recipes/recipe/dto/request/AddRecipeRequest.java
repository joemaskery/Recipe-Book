package org.recipes.recipe.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddRecipeRequest {

    private final String name;
    private final String description;
    private final String instructions;
    private final String weblink;
    private final List<IngredientInput> ingredients;

}
