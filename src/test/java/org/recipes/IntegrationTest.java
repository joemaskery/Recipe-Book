package org.recipes;

import io.restassured.RestAssured;
import org.recipes.user.entity.User;
import org.recipes.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    Integer port;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

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

    @AfterAll
    static void afterAll() {
        mariaDBContainer.stop();
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;
        jdbcTemplate.execute("TRUNCATE TABLE users");

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
