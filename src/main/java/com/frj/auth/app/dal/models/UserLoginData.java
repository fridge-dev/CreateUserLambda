package com.frj.auth.app.dal.models;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginData that = (UserLoginData) o;
        return username.equals(that.username) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}