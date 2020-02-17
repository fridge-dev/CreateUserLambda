package com.frj.auth.app.specs;

import com.frj.auth.app.CreateUserRequest;
import java.util.regex.Pattern;

/**
 * Responsible for validating username/password specifications.
 *
 * @author fridge
 */
public class SpecValidator {

    /**
     * Total length: 4-16 chars
     * Starts with: a-Z
     * Contains: a-Z0-9
     */
    private static final Pattern USERNAME_SIMPLE_SPEC = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{3,15}");

    private static final Pattern PASSWORD_PIN_SPEC = Pattern.compile("[0-9]{5,8}");

    public static boolean isUsernameValid(final CreateUserRequest.UsernameSpec usernameSpec, final String username) {
        switch (usernameSpec) {
            case SIMPLE:
                return USERNAME_SIMPLE_SPEC.matcher(username).matches();
            case EMAIL:
                return username.contains("@"); // I'm lazy
            default:
                throw new UnsupportedOperationException("Missing branch for " + usernameSpec);
        }
    }

    public static boolean isPasswordValid(final CreateUserRequest.PasswordSpec passwordSpec, final String password) {
        switch (passwordSpec) {
            case PIN:
                return PASSWORD_PIN_SPEC.matcher(password).matches();
            case COMPLEX:
                return true;
            default:
                throw new UnsupportedOperationException("Missing branch for " + passwordSpec);
        }
    }
}
