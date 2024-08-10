package org.recipes;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.recipes.recipe.repository.RecipeRepository;
import org.recipes.user.entity.User;
import org.recipes.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;

import java.util.List;

import static org.recipes.testutils.RecipeTestBuilder.pizza;
import static org.recipes.testutils.RecipeTestBuilder.tomatoPasta;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort Integer port;
    @Autowired UserRepository userRepository;
    @Autowired RecipeRepository recipeRepository;
    @Autowired JdbcTemplate jdbcTemplate;

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
        saveUserEntities();
        saveRecipeEntities();
    }

    @AfterEach
    void afterEach() {
        resetTables();
    }

    private void resetTables() {
        jdbcTemplate.execute("DELETE FROM ingredients");
        jdbcTemplate.execute("ALTER TABLE ingredients AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM recipes");
        jdbcTemplate.execute("ALTER TABLE recipes AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1");
    }

    private void saveRecipeEntities() {
        recipeRepository.saveAll(
                List.of(tomatoPasta(1).build(), pizza(2).build())
        );
    }

    private void saveUserEntities() {
        User user1 = User.builder()
                .firstName("name1")
                .secondName("surname1")
                .email("email1@domain.com")
                .build();
        User user2 = User.builder()
                .firstName("name2")
                .secondName("surname2")
                .email("email2@domain.com")
                .build();

        userRepository.saveAll(List.of(user1, user2));
    }

}
