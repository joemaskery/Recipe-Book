package org.recipes;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.recipes.recipe.entity.IngredientEntity;
import org.recipes.recipe.repository.IngredientReferenceRepository;
import org.recipes.recipe.repository.RecipeIngredientRepository;
import org.recipes.recipe.repository.RecipeRepository;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;

import java.util.List;

import static org.recipes.testutils.IngredientTestBuilder.*;
import static org.recipes.testutils.RecipeIngredientTestBuilder.*;
import static org.recipes.testutils.RecipeTestBuilder.pizza;
import static org.recipes.testutils.RecipeTestBuilder.tomatoPastaEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort Integer port;
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired UserRepository userRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired IngredientReferenceRepository ingredientRepository;
    @Autowired RecipeIngredientRepository recipeIngredientRepository;

    static MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:11.2.2");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDBContainer::getUsername);
        registry.add("spring.datasource.password", mariaDBContainer::getPassword);
        registry.add("spring.flyway.url", mariaDBContainer::getJdbcUrl);
        registry.add("spring.flyway.user", mariaDBContainer::getUsername);
        registry.add("spring.flyway.password", mariaDBContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        mariaDBContainer.start();
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;
        resetTables();
        saveUsers();
        saveRecipes();
    }

    private void resetTables() {
        jdbcTemplate.execute("DELETE FROM recipe_ingredients");
        jdbcTemplate.execute("ALTER TABLE recipe_ingredients AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM recipes");
        jdbcTemplate.execute("ALTER TABLE recipes AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM ingredients_reference");
        jdbcTemplate.execute("ALTER TABLE ingredients_reference AUTO_INCREMENT = 1");
    }

    private void saveUsers() {
        UserEntity user1 = UserEntity.builder()
                .firstName("name1")
                .secondName("surname1")
                .email("email1@domain.com")
                .build();
        UserEntity user2 = UserEntity.builder()
                .firstName("name2")
                .secondName("surname2")
                .email("email2@domain.com")
                .build();

        userRepository.saveAll(List.of(user1, user2));
    }

    private void saveRecipes() {
        // save ingredient references
        final IngredientEntity tomato = ingredientRepository.save(tomatoReference());
        final IngredientEntity pasta = ingredientRepository.save(pastaReference());
        final IngredientEntity cheese = ingredientRepository.save(cheddarCheeseReference());
        final IngredientEntity garlicBread = ingredientRepository.save(garlicBreadReference());
        final IngredientEntity pizzaDough = ingredientRepository.save(pizzaDoughReference());

        // save recipes
        final Integer tomatoPastaId = recipeRepository.save(tomatoPastaEntity(1).build()).getRecipeId();
        final Integer pizzaId = recipeRepository.save(pizza(2).build()).getRecipeId();

        // save recipe ingredients
        recipeIngredientRepository.saveAll(List.of(
                tomatoEntity(tomatoPastaId, tomato.getReferenceId(), 1.0),
                pastaEntity(tomatoPastaId, pasta.getReferenceId(), 100.0),
                cheeseEntity(tomatoPastaId, cheese.getReferenceId(), 30.0),
                garlicBreadEntity(tomatoPastaId, garlicBread.getReferenceId(), 0.5))
        );

        recipeIngredientRepository.saveAll(List.of(
                tomatoEntity(pizzaId, tomato.getReferenceId(), 5.0),
                pizzaDoughEntity(pizzaId, pizzaDough.getReferenceId(), 300.0),
                cheeseEntity(pizzaId, cheese.getReferenceId(), 200.0))
        );
    }

}
