package org.recipes.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.model.QuantityType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "recipe_ingredients")
public class RecipeIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;
    private Integer recipeId;
    private Integer ingredientRefId;
    private Double quantity;
    @Enumerated(EnumType.STRING)
    private QuantityType quantityType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredientRefId", referencedColumnName = "ingredient_ref_id", insertable = false, updatable = false)
    private IngredientEntity ingredientReference;

}
