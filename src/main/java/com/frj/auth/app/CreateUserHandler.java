package com.frj.auth.app;

import com.frj.auth.app.dal.models.DataAccessor;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import com.frj.auth.app.dal.models.UserLoginData;
import com.frj.auth.app.password.PasswordHasher;
import com.frj.auth.app.password.models.CannotPerformHashException;
import com.frj.auth.app.password.models.InvalidHashException;
import com.frj.auth.app.specs.SpecValidator;
import java.util.Objects;

/**
 * The application layer business logic for creating a user.
 *
 * @author fridge
 */
public class CreateUserHandler {

    private final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor;

    private final PasswordHasher passwordHasher;

    public CreateUserHandler(
            final DataAccessor<UserLoginDataKey, UserLoginData> userLoginDataAccessor,
            final PasswordHasher passwordHasher
    ) {
        this.userLoginDataAccessor = Objects.requireNonNull(userLoginDataAccessor);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
    }

    public CreateUserReply createUser(final CreateUserRequest createUserRequest) {
        validateRequest(createUserRequest);

        final String storableHash = hashPassword(createUserRequest.getPassword());

        userLoginDataAccessor.create(new UserLoginData(
                createUserRequest.getUsername(),
                storableHash
        ));

        return new CreateUserReply("");
    }

    private String hashPassword(final String rawPassword) {
        final String storableHash;
        try {
            storableHash = passwordHasher.createStorableHash(rawPassword);
        } catch (InvalidHashException | CannotPerformHashException e) {
            // TODO
            throw new RuntimeException("Failed to create hash", e);
        }

        // Sanity check that we hashed correctly.
        try {
            passwordHasher.matches(rawPassword, storableHash);
        } catch (InvalidHashException | CannotPerformHashException e) {
            // TODO
            throw new RuntimeException("Failed to validate hash", e);
        }

        return storableHash;
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
