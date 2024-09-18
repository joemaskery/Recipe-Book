package org.recipes.user.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.recipes.IntegrationTest;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.user.dto.UpdateUserRequest;
import org.recipes.user.dto.User;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.recipes.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIntTest extends IntegrationTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    @Test
    void canGetAllUsers() {
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
    void canGetUserById() {
        // when
        Response response = given().get("/user/get/2");
        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody().as(User.class))
                .extracting("userId", "firstName", "secondName", "email")
                .containsExactly(2, "name2", "surname2", "email2@domain.com");
    }

    @Test
    void canAddUser() {
        // given
        AddUserRequest request = AddUserRequest.builder()
                .firstName("firstName")
                .secondName("secondName")
                .email("test@email.com")
                .password1("a-password")
                .password2("a-password")
                .build();

        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
            .when()
                .post("/user/add");

        final User userResponse = response.getBody().as(User.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);

        final UserEntity savedUser = userRepository.findById(userResponse.getUserId()).get();
        assertThat(savedUser.getUserId()).isEqualTo(3);
        assertThat(savedUser.getEmail()).isEqualTo("test@email.com");
        assertThat(savedUser.getFirstName()).isEqualTo("firstName");
        assertThat(savedUser.getSecondName()).isEqualTo("secondName");
        assertThat(savedUser.getPassword()).isNotBlank();
    }

    @Test
    void canDeleteUser() {
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
        assertThat(userRepository.findById(1).get()).usingRecursiveComparison().ignoringFields("recipes")
                .isEqualTo(updatedUser);
    }

    @ParameterizedTest
    @CsvSource(value = {"a-password, true", "a-bad-password, false"})
    void canCheckUserPassword(String password, Boolean expectedResult) {
        // given
        AddUserRequest request = AddUserRequest.builder()
                .firstName("firstName")
                .secondName("secondName")
                .email("test@email.com")
                .password1("a-password")
                .password2("a-password")
                .build();
        userService.addUser(request);

        // when
        Response response = given()
                .body(request)
                .param("userId", 3)
                .param("userPassword", password)
                .contentType(ContentType.JSON)
            .when()
                .get("/user/check-password");

        // then
        assertThat(response.getBody().as(Boolean.class)).isEqualTo(expectedResult);
    }

}
