package org.recipes.commons.exception;

public class UserValidationException extends RuntimeException {

    public UserValidationException(final String errorMessage) {
        super(errorMessage);
    }
}
