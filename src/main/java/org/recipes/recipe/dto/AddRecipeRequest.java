package org.recipes.recipe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddRecipeRequest {

    private final Integer userId;
    private final String name;
    private final String description;
    private final String weblink;
    private final List<IngredientInput> ingredients;

}
