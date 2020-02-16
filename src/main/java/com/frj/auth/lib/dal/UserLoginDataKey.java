package com.frj.auth.lib.dal;

import java.util.Objects;

/**
 * Key for loading a user.
 *
 * @author fridge
 */
public class UserLoginDataKey implements AppDataKey {

    private final String username;

    public UserLoginDataKey(final String username) {
        this.username = Objects.requireNonNull(username);
    }

    public String getUsername() {
        return username;
    }
}