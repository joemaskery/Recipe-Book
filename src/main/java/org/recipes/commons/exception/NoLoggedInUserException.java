package org.recipes.commons.exception;

public class NoLoggedInUserException extends RuntimeException {

    public NoLoggedInUserException(final String errorMessage) {
        super(errorMessage);
    }
}
