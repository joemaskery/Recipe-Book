package org.recipes.testutils;

import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.recipes.testutils.builder.IngredientTestBuilder.*;

@Service
public class IngredientHelper {

    @Autowired IngredientReferenceRepository ingredientRepository;

    public List<IngredientEntity> saveIngredients() {
        return ingredientRepository.saveAll(List.of(
                tomatoReference(), pastaReference(), cheddarCheeseReference(), garlicBreadReference(),
                pizzaDoughReference()
        ));
    }

}
