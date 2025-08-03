package org.recipes.user.repository;

import org.junit.jupiter.api.Test;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.entity.RecipeEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.RecipeRepository;
import org.recipes.user.dto.UserWithStats;
import org.recipes.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.builder.IngredientTestBuilder.cheddarCheeseReference;
import static org.recipes.testutils.builder.IngredientTestBuilder.pastaReference;
import static org.recipes.testutils.builder.IngredientTestBuilder.tomatoReference;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.cheeseEntity;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.pastaEntity;
import static org.recipes.testutils.builder.RecipeIngredientTestBuilder.tomatoEntity;
import static org.recipes.testutils.builder.RecipeTestBuilder.pizzaRecipeEntity;
import static org.recipes.testutils.builder.RecipeTestBuilder.tomatoPastaRecipeEntity;
import static org.recipes.testutils.builder.UserEntityTestBuilder.userEntity;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientReferenceRepository ingredientReferenceRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;

    @Test
    void findUserWithStatsByEmail() {
        // given
        final UserEntity user = userRepository.save(userEntity().build());

        final RecipeEntity pastaRecipe = recipeRepository.save(tomatoPastaRecipeEntity(user.getUserId()).build());
        final RecipeEntity pizzaRecipe = recipeRepository.save(pizzaRecipeEntity(user.getUserId()).build());

        final IngredientEntity tomatoRef = ingredientReferenceRepository.save(tomatoReference());
        final IngredientEntity pastaRef = ingredientReferenceRepository.save(pastaReference());
        final IngredientEntity cheeseRef = ingredientReferenceRepository.save(cheddarCheeseReference());

        recipeIngredientRepository.save(tomatoEntity(pastaRecipe.getRecipeId(), tomatoRef.getReferenceId(), 3.0));
        recipeIngredientRepository.save(pastaEntity(pastaRecipe.getRecipeId(), pastaRef.getReferenceId(), 100.0));

        recipeIngredientRepository.save(tomatoEntity(pizzaRecipe.getRecipeId(), tomatoRef.getReferenceId(), 50.0));
        recipeIngredientRepository.save(cheeseEntity(pizzaRecipe.getRecipeId(), cheeseRef.getReferenceId(), 300.0));

        // when
        final Optional<UserWithStats> result = userRepository.findUserWithStatsByEmail(user.getEmail());

        // then
        assertThat(result).get()
                .isEqualTo(UserWithStats.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .secondName(user.getSecondName())
                        .email(user.getEmail())
                        .dateJoined(user.getCreatedDate())
                        .recipes(2L)
                        .ingredients(3L)
                        .build());
    }
}