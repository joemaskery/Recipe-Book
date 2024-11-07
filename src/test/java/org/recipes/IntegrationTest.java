package org.recipes;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort Integer port;
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
        RestAssured.defaultParser = Parser.JSON;
        resetTables();
    }

    private void resetTables() {
        jdbcTemplate.execute("DELETE FROM recipe_ingredients");
        jdbcTemplate.execute("ALTER TABLE recipe_ingredients AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM recipes");
        jdbcTemplate.execute("ALTER TABLE recipes AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM ingredients_reference");
        jdbcTemplate.execute("ALTER TABLE ingredients_reference AUTO_INCREMENT = 1");

        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1");
    }

}
