package com.frj.auth.app.password.models;

import com.frj.auth.app.password.PasswordHasher;

/**
 * Kind of like {@link IllegalArgumentException} when providing a hash to the {@link PasswordHasher} for verification.
 *
 * @author fridge
 */
public class InvalidHashException extends Exception {
    public InvalidHashException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public InvalidHashException(final String message) {
        super(message);
    }
    public InvalidHashException(final Throwable cause) {
        super(cause);
    }
}