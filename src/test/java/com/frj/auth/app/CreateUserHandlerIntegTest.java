package com.frj.auth.app;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.frj.auth.app.dal.ddb.UserLoginCredsDdbItem;
import com.frj.auth.app.dal.models.ConditionalWriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Functional integ test woohoo.
 *
 * @author fridge
 */
public class CreateUserHandlerIntegTest {

    private static final TestUtilLocalAppModuleFactory APP_MODULE_FACTORY = new TestUtilLocalAppModuleFactory(UserLoginCredsDdbItem.class);

    private CreateUserHandler createUserHandler;

    @BeforeEach
    void setup() {
        createUserHandler = APP_MODULE_FACTORY.create().getCreateUserHandler();
    }

    @Test
    void createUser() {
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "23456",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        final CreateUserReply reply = createUserHandler.createUser(req);

        assertNotNull(reply);
        assertNotNull(reply.getFailureMessage());
    }

    @Test
    void createUser_Duplicate() {
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "23456",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        createUserHandler.createUser(req);
        assertThrows(ConditionalWriteException.class, () -> createUserHandler.createUser(req));
    }

    @Test
    void createUser_InvalidUsernameSpec() {
        final CreateUserRequest req = new CreateUserRequest(
                "afoshaw48fhoa9j4fap9j4f",
                "23456",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        final IllegalArgumentException caught = assertThrows(IllegalArgumentException.class, () -> createUserHandler.createUser(req));
        assertTrue(caught.getMessage().contains("username"));
    }

    @Test
    void createUser_InvalidPasswordSpec() {
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "123-345",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        final IllegalArgumentException caught = assertThrows(IllegalArgumentException.class, () -> createUserHandler.createUser(req));
        assertTrue(caught.getMessage().contains("password"));
    }
}
