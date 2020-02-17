package com.frj.auth.app;

import com.frj.auth.app.dal.models.DataAccessor;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import com.frj.auth.app.dal.models.UserLoginData;
import java.util.Objects;

/**
 * The application layer business logic for creating a user.
 *
 * @author fridge
 */
public class CreateUserHandler {

    private final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor;

    public CreateUserHandler(final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor) {
        this.userLoginDataAccessor = Objects.requireNonNull(userLoginDataAccessor);
    }

    public CreateUserReply createUser(final CreateUserRequest createUserRequest) {
        userLoginDataAccessor.create(new UserLoginData(createUserRequest.getUsername(), createUserRequest.getPassword()));

        final String msg = String.format(
                "You want me to create user %s with pw %s and spec %s / %s? Frick no! (because we don't have DB support)",
                createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getUsernameSpec(),
                createUserRequest.getPasswordSpec()
        );

        System.out.println(msg);

        return new CreateUserReply(msg);
    }

}
