package com.frj.auth.lib;

import com.frj.auth.lib.dal.DataAccessor;
import com.frj.auth.lib.dal.UserLoginDataKey;
import com.frj.auth.lib.dal.UserLoginData;
import java.util.Objects;

/**
 * TODO
 *
 * @author TODO
 */
public class UserCreator {

    private final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor;

    public UserCreator(final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor) {
        this.userLoginDataAccessor = Objects.requireNonNull(userLoginDataAccessor);
    }

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
