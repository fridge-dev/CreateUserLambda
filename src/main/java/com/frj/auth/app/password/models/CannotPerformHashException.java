package com.frj.auth.app.password.models;

import com.frj.auth.app.password.PasswordHasher;

/**
 * Exception thrown when {@link PasswordHasher} fails to perform a hash.
 *
 * @author fridge
 */
public class CannotPerformHashException extends Exception {
    public CannotPerformHashException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public CannotPerformHashException(final String message) {
        super(message);
    }
    public CannotPerformHashException(final Throwable cause) {
        super(cause);
    }
}