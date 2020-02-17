package com.frj.auth.app;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.frj.auth.app.dal.UserLoginDataAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateUserHandlerTest {

    private CreateUserHandler createUserHandler;

    @BeforeEach
    void setup() {
        createUserHandler = new CreateUserHandler(UserLoginDataAccessor.getAccessor(new FakeDynamoDBMapper()));
    }

    @Test
    void test() {
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "2345",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        final CreateUserReply reply = createUserHandler.createUser(req);

        assertNotNull(reply);
    }

}