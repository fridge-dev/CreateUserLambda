package com.frj.auth.lambda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.frj.auth.app.CreateUserHandler;
import com.frj.auth.app.CreateUserReply;
import com.frj.auth.app.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateUserLambdaHandlerTest {

    @InjectMocks
    private CreateUserLambdaHandler createUserLambdaHandler;

    @Mock
    private CreateUserHandler injectedCreateUserHandler;

    @Captor
    private ArgumentCaptor<CreateUserRequest> internalRequestCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void handleRequest() {
        final CreateUserLambdaRequest request = new CreateUserLambdaRequest();
        request.setUsername("my-user-n");
        request.setPassword("jfpq89w4jfq4f");
        request.setSpec("SIMPLE/PIN");

        when(injectedCreateUserHandler.createUser(internalRequestCaptor.capture())).thenReturn(new CreateUserReply(""));

        final CreateUserLambdaReply reply = createUserLambdaHandler.handleRequest(request, new NullLambdaContext());

        assertNotNull(reply);
        assertNotNull(reply.getFailureMessage());
        assertEquals("my-user-n", internalRequestCaptor.getValue().getUsername());
        assertEquals("jfpq89w4jfq4f", internalRequestCaptor.getValue().getPassword());
        assertEquals(CreateUserRequest.UsernameSpec.SIMPLE, internalRequestCaptor.getValue().getUsernameSpec());
        assertEquals(CreateUserRequest.PasswordSpec.PIN, internalRequestCaptor.getValue().getPasswordSpec());
    }
}