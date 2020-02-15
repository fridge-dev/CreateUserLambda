package com.frj.auth.lib;

/**
 * TODO
 *
 * @author TODO
 */
public class UserCreator {

    public CreateUserReply createUser(final CreateUserRequest createUserRequest) {
        final String msg = String.format(
                "You want me to create user %s with pw %s and spec %s / %s? Frick no!",
                createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getUsernameSpec(),
                createUserRequest.getPasswordSpec()
        );

        System.out.println(msg + " - sout");

        return new CreateUserReply(msg);
    }

}
