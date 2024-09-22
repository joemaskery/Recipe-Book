package org.recipes.recipe.repository;

import org.recipes.recipe.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientReferenceRepository extends JpaRepository<IngredientEntity, Integer> {

    List<IngredientEntity> findAllByUserIdEqualsOrAllUsersIsTrue(Integer usedId);

}
