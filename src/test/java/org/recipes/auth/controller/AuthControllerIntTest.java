package org.recipes.auth.controller;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.auth.dto.LoginRequest;
import org.recipes.auth.dto.LoginResponse;
import org.recipes.user.dto.User;
import org.recipes.user.entity.UserEntity;
import org.recipes.user.repository.UserRepository;
import org.recipes.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerIntTest extends IntegrationTest {

    private static final String USER_EMAIL = "user-email@email.com";
    private static final String USER_PASSWORD = "user-password";
    private static final String USER_PASSWORD_INCORRECT = "this-password-is-wrong";

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    void user_can_register() {
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
                .post("/auth/register");

        final User userResponse = response.getBody().as(User.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);

        final UserEntity savedUser = userRepository.findById(userResponse.getUserId()).get();
        assertThat(savedUser.getEmail()).isEqualTo("test@email.com");
        assertThat(savedUser.getFirstName()).isEqualTo("firstName");
        assertThat(savedUser.getSecondName()).isEqualTo("secondName");
        assertThat(savedUser.getPassword()).isNotBlank();
    }


    @Test
    void user_login_provides_token_for_correct_password() {
        // given
        givenUserExists(USER_EMAIL, USER_PASSWORD);

        final LoginRequest request = LoginRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
            .when()
                .post("/auth/login");

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);

        final LoginResponse loginResponse = response.getBody().as(LoginResponse.class);
        assertThat(loginResponse.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(loginResponse.getToken()).isNotNull();
    }

    @Test
    void login_returns_401_if_password_is_incorrect() {
        // given
        givenUserExists(USER_EMAIL, USER_PASSWORD);

        final LoginRequest request = LoginRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD_INCORRECT)
                .build();

        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
            .when()
                .post("/auth/login");

        // then
        assertThat(response.getStatusCode()).isEqualTo(401);
    }

    @Test
    void login_returns_401_if_user_does_not_exist() {
        // given
        final LoginRequest request = LoginRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        // when
        Response response = given()
                .body(request)
                .contentType(ContentType.JSON)
            .when()
                .post("/auth/login");

        // then
        assertThat(response.getStatusCode()).isEqualTo(401);
    }

    private User givenUserExists(final String email, final String password) {
        return userService.addUser(
                AddUserRequest.builder()
                        .firstName("firstName")
                        .secondName("secondName")
                        .email(email)
                        .password1(password)
                        .password2(password)
                        .build()
        );
    }

}
