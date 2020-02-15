package com.frj.auth.lib;

/**
 * TODO
 *
 * @author TODO
 */
public final class Module {

    private final UserCreator USER_CREATOR = makeUserCreator();

    public UserCreator getUserCreator() {
        return USER_CREATOR;
    }

    private UserCreator makeUserCreator() {
        return new UserCreator();
    }

}
