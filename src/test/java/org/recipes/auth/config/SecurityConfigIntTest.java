package org.recipes.auth.config;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.recipes.IntegrationTest;
import org.recipes.auth.service.AuthService;
import org.recipes.user.dto.AddUserRequest;
import org.recipes.auth.dto.LoginResponse;
import org.recipes.user.dto.User;
import org.recipes.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SecurityConfigIntTest extends IntegrationTest {

    private static final String USER_EMAIL = "user-email@email.com";
    private static final String USER_PASSWORD = "user-password";

    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @Test
    void unauthorized_request_returns_403() {
        // given, when
        Response response = given()
                .get("/user/get-all");
        // then
        assertThat(response.getStatusCode()).isEqualTo(401);
    }

    @Test
    void authorized_request_returns_200() {
        // given
        final String token = givenAuthenticatedUserExists(USER_EMAIL, USER_PASSWORD);
        final String headerToken = String.format("Bearer %s", token);
        // when
        Response response = given()
                .header("authorization", headerToken)
                .get("/user/get-all");
        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
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

    private String givenAuthenticatedUserExists(final String email, final String password) {
        givenUserExists(email, password);
        final LoginResponse loginResponse = authService.login(email, password);
        return loginResponse.getToken();
    }

}
