package org.recipes.recipe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecipe {

    private Integer recipeId;
    private Integer userId;
    private String name;
    private String description;
    private String weblink;
    private LocalDate createdDate;
    private List<RecipeIngredient> ingredients;
}
