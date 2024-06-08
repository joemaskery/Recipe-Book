package org.blog.user.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.blog.IntegrationTest;
import org.blog.user.dto.AddUserRequest;
import org.blog.user.dto.UpdateUserRequest;
import org.blog.user.entity.User;
import org.blog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerIntTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

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
        Response response = given()
                .get("/user/get/2");

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

        User userResponse = response.getBody().as(User.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(userResponse.getUserId()).isEqualTo(3);
        assertThat(userResponse.getEmail()).isEqualTo("test@email.com");
        assertThat(userResponse.getFirstName()).isEqualTo("firstName");
        assertThat(userResponse.getSecondName()).isEqualTo("secondName");
        assertThat(userResponse.getPassword()).isNotBlank();
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
        User updatedUser = User.builder()
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
        assertThat(response.getBody().as(User.class)).isEqualTo(updatedUser);
        assertThat(userRepository.findById(1).get()).isEqualTo(updatedUser);
    }

}
