package org.recipes.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.recipes.recipe.model.QuantityType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ingredientId;
    private String name;
    private Double quantity;
    @Enumerated(EnumType.STRING)
    private QuantityType quantityType;

}
