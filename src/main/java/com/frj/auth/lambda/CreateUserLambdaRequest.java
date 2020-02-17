package com.frj.auth.lambda;

import java.util.Objects;

/**
 * Lambda (presentation layer) input for {@link CreateUserLambdaHandler}.
 *
 * @author fridge
 */
public class CreateUserLambdaRequest {

    private String username;
    private String password;
    private String spec;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = Objects.requireNonNull(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = Objects.requireNonNull(password);
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(final String spec) {
        this.spec = Objects.requireNonNull(spec);
    }
}
