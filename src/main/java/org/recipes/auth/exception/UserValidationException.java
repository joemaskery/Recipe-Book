package org.recipes.auth.exception;

public class UserValidationException extends RuntimeException {

    public UserValidationException(final String errorMessage) {
        super(errorMessage);
    }
}
