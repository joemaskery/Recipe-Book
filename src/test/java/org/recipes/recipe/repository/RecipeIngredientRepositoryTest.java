package org.recipes.recipe.repository;

import org.junit.jupiter.api.Test;
import org.recipes.commons.model.QuantityType;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.repository.dao.IngredientSummary;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.builder.IngredientTestBuilder.tomatoReference;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.tomatoEntity;
import static org.recipes.testutils.builder.RecipeTestBuilder.tomatoPastaRecipeEntity;
import static org.recipes.testutils.builder.UserEntityTestBuilder.userEntity;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RecipeIngredientRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;
    @Autowired IngredientReferenceRepository ingredientReferenceRepository;

    @Test
    void findAllByRecipeIdIn_returns_expected_ingredient_summaries() {
        // given
        final UserEntity user = userRepository.save(userEntity().build());
        final RecipeEntity recipe = recipeRepository.save(tomatoPastaRecipeEntity(user.getUserId()).build());
        final IngredientEntity tomatoRef = ingredientReferenceRepository.save(tomatoReference());

        recipeIngredientRepository.save(tomatoEntity(recipe.getRecipeId(), tomatoRef.getReferenceId(), 3.0));

        // when
        final List<IngredientSummary> summaries =
                recipeIngredientRepository.findAllByRecipeIdIn(List.of(recipe.getRecipeId()));

        // then
        assertThat(summaries).containsExactlyInAnyOrder(
                new IngredientSummary(tomatoRef.getReferenceId(), 3.0, QuantityType.ITEMS, tomatoRef.getName(),
                        tomatoRef.getCategory())
        );
    }

}
