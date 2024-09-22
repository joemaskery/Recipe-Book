package org.recipes.testutils;

import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.recipes.testutils.RecipeIngredientTestBuilder.*;
import static org.recipes.testutils.RecipeTestBuilder.pizza;
import static org.recipes.testutils.RecipeTestBuilder.tomatoPastaEntity;

@Service
public class RecipeHelper {

    @Autowired RecipeRepository recipeRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;
    @Autowired UserHelper userHelper;
    @Autowired IngredientHelper ingredientHelper;

    public void saveRecipes() {
        userHelper.saveUsers();
        final List<IngredientEntity> savedIngredients = ingredientHelper.saveIngredients();
        final Map<String, Integer> ingredientIdsByName = savedIngredients.stream()
                .collect(Collectors.toMap(IngredientEntity::getName, IngredientEntity::getReferenceId));

        // save recipes
        final Integer tomatoPastaId = recipeRepository.save(tomatoPastaEntity(1).build()).getRecipeId();
        final Integer pizzaId = recipeRepository.save(pizza(2).build()).getRecipeId();

        // save recipe ingredients
        recipeIngredientRepository.saveAll(List.of(
                tomatoEntity(tomatoPastaId, ingredientIdsByName.get("Tomato"), 1.0),
                pastaEntity(tomatoPastaId, ingredientIdsByName.get("Fusilli Pasta"), 100.0),
                cheeseEntity(tomatoPastaId, ingredientIdsByName.get("Cheddar Cheese"), 30.0),
                garlicBreadEntity(tomatoPastaId, ingredientIdsByName.get("Garlic Bread"), 0.5))
        );

        recipeIngredientRepository.saveAll(List.of(
                tomatoEntity(pizzaId, ingredientIdsByName.get("Tomato"), 5.0),
                pizzaDoughEntity(pizzaId, ingredientIdsByName.get("Pizza Reference"), 300.0),
                cheeseEntity(pizzaId, ingredientIdsByName.get("Cheddar Cheese"), 200.0))
        );
    }

}
