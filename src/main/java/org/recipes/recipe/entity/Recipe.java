package org.recipes.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "recipes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recipeId;
    private Integer userId;
    private String name;
    private String description;
    @Column(name = "link")
    private String weblink;

    @Transient
    private List<Ingredient> ingredients;

}
