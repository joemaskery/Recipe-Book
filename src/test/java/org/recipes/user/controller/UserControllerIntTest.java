package org.recipes.user.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.auth.security.JwtHelper;
import org.recipes.testutils.RecipeHelper;
import org.recipes.testutils.UserHelper;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
import org.recipes.user.dto.UserWithStats;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.recipes.testutils.UserHelper.USER_1;

@ActiveProfiles("test")
public class UserControllerIntTest extends IntegrationTest {

    @Autowired UserRepository userRepository;
    @Autowired UserHelper userHelper;
    @Autowired RecipeHelper recipeHelper;

    @Test
    void canGetAllUsers() {
        // given
        userHelper.saveUsers();

        // when
        Response response = given()
                .get("/user/get-all");

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().as(User[].class))
                .extracting(User::getUserId)
                .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    void getUserWithStats_returns_user_details_for_valid_token() {
        // given
        recipeHelper.saveRecipes();

        final String token = String.format("Bearer %s", JwtHelper.generateToken(USER_1.getEmail()));

        // when
        Response response = given()
                    .headers("Authorization", token)
                .when()
                    .get("/user/get-with-stats");
        final UserWithStats userResponse = response.as(UserWithStats.class);

        // then
        final UserEntity user = userRepository.findById(USER_1.getUserId()).get();

        assertThat(userResponse).isEqualTo(UserWithStats.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .email(user.getEmail())
                .dateJoined(user.getCreatedDate())
                .recipes(1L)
                .ingredients(4L)
                .build());
    }

    @Test
    void canGetUserById() {
        // given
        userHelper.saveUsers();
        // when
        Response response = given().get("/user/get/2");
        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().as(User.class))
                .extracting("userId", "firstName", "secondName", "email")
                .containsExactly(2, "name2", "surname2", "email2@domain.com");
    }

    @Test
    void canDeleteUser() {
        // given
        userHelper.saveUsers();
        // when
        Response response = given()
                .delete("/user/delete/2");

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().asString()).isEqualTo("Successfully deleted user 2");
        assertThat(userRepository.existsById(2)).isFalse();
    }

    @Test
    void canUpdateUser() {
        // given
        userHelper.saveUsers();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .userId(1)
                .firstName("newFirstName")
                .secondName("newSecondName")
                .email("newemail@domain.com")
                .build();
        UserEntity updatedUser = UserEntity.builder()
                .userId(1)
                .firstName("newFirstName")
                .secondName("newSecondName")
                .email("newemail@domain.com")
                .build();

        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put("/user/update");

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(userRepository.findById(1).get()).usingRecursiveComparison()
                .ignoringFields("recipes", "createdDate")
                .isEqualTo(updatedUser);
    }
}
