package com.frj.auth.app;

import java.util.Objects;

/**
 * The application model for {@link CreateUserHandler}.
 *
 * @author fridge
 */
public class CreateUserRequest {

    public enum UsernameSpec {
        EMAIL,
        SIMPLE;
    }

    public enum PasswordSpec {
        PIN,
        COMPLEX;
    }

    private final String username;
    private final String password;
    private final UsernameSpec usernameSpec;
    private final PasswordSpec passwordSpec;

    public CreateUserRequest(
            final String username,
            final String password,
            final UsernameSpec usernameSpec,
            final PasswordSpec passwordSpec
    ) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.usernameSpec = Objects.requireNonNull(usernameSpec);
        this.passwordSpec = Objects.requireNonNull(passwordSpec);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UsernameSpec getUsernameSpec() {
        return usernameSpec;
    }

    public PasswordSpec getPasswordSpec() {
        return passwordSpec;
    }
}
