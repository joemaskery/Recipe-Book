package org.recipes.auth.security;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtHelperTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"token-without-bearer"})
    void extractUsernameWithBearer_throws_exception_for_invalid_token(final String token) {
        assertThatThrownBy(() -> JwtHelper.extractUsernameWithBearer(token))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("Invalid token signature");
    }
}