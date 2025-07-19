package org.recipes.commons.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
