package com.frj.auth.app.password.models;

import com.frj.auth.app.password.PasswordHasher;

/**
 * Exception thrown when {@link PasswordHasher} fails to perform a hash.
 *
 * @author fridge
 */
public class PasswordHashException extends Exception {
    public PasswordHashException(final String message, final Throwable cause) {
        super(message, cause);
    }
    public PasswordHashException(final String message) {
        super(message);
    }
    public PasswordHashException(final Throwable cause) {
        super(cause);
    }
}