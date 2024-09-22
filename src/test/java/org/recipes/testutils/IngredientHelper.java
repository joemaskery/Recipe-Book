package org.recipes.testutils;

import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.recipes.testutils.IngredientTestBuilder.*;

@Service
public class IngredientHelper {

    @Autowired IngredientReferenceRepository ingredientRepository;
    @Autowired UserHelper userHelper;

    public List<IngredientEntity> saveIngredients() {
        userHelper.saveUsers();

        // save ingredient references
        return ingredientRepository.saveAll(List.of(
                tomatoReference(), pastaReference(), cheddarCheeseReference(), garlicBreadReference(),
                pizzaDoughReference()
        ));
    }

}
