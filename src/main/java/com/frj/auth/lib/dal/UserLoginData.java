package com.frj.auth.lib.dal;

import java.util.Objects;

/**
 * Application POJO representing a user.
 *
 * @author fridge
 */
public class UserLoginData implements AppDataModel {

    private final String username;
    private final String password;

    public UserLoginData(final String username, final String password) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}