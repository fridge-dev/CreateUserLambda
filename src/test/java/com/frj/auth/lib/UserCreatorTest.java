package com.frj.auth.lib;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCreatorTest {

    private UserCreator userCreator;

    @BeforeEach
    void setup() {
        userCreator = new UserCreator();
    }

    @Test
    void test() {
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "2345",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );

        final CreateUserReply reply = userCreator.createUser(req);

        assertNotNull(reply);
    }

}