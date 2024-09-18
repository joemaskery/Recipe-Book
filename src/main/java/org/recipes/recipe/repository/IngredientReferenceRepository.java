package org.recipes.recipe.repository;

import org.recipes.recipe.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientReferenceRepository extends JpaRepository<IngredientEntity, Integer> {

}
