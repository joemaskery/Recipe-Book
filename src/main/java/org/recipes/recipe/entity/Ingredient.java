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
@Entity(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;
    private Integer recipeId;
    private String name;
    private Double quantity;
    @Enumerated(EnumType.STRING)
    private QuantityType quantityType;

}
