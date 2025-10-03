package org.recipes.recipe.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddRecipeRequest {

    @NotBlank(message = "Recipe name must not be blank")
    private final String name;

    @NotBlank(message = "Recipe description must not be blank")
    private final String description;

    private final String instructions;

    private final String weblink;

    @Valid
    @NotEmpty(message = "Recipe must have at least one ingredient")
    private final List<IngredientInput> ingredients;
}
