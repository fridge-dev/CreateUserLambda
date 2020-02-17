package com.frj.auth.app;

import com.frj.auth.app.dal.models.DataAccessor;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import com.frj.auth.app.dal.models.UserLoginData;
import com.frj.auth.app.specs.SpecValidator;
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
        validateRequest(createUserRequest);

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

    private void validateRequest(final CreateUserRequest request) {
        if (!SpecValidator.isUsernameValid(request.getUsernameSpec(), request.getUsername())) {
            throw new IllegalArgumentException("Provided username does not fulfill the required specifications.");
        }
        if (!SpecValidator.isPasswordValid(request.getPasswordSpec(), request.getPassword())) {
            throw new IllegalArgumentException("Provided password does not fulfill the required specifications.");
        }
    }
}
