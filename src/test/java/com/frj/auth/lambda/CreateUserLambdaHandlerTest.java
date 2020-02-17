package com.frj.auth.lambda;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;

class CreateUserLambdaHandlerTest {

    private CreateUserLambdaHandler createUserLambdaHandler;

    @BeforeEach
    void setup() {
        /*
         * TODO How do we instantiate this class while mocking the DB?
         */
        createUserLambdaHandler = new CreateUserLambdaHandler();
    }

    void handleRequest() {
        final CreateUserLambdaRequest request = new CreateUserLambdaRequest();
        request.setUsername("my-user-n");
        request.setPassword("jfpq89w4jfq4f");
        request.setSpec("aosiejfoaseijf");

        final CreateUserLambdaReply reply = createUserLambdaHandler.handleRequest(request, new NullLambdaContext());

        assertNotNull(reply);
    }
}