package com.frj.auth.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.frj.auth.app.dal.models.DataAccessor;
import com.frj.auth.app.dal.models.UserLoginData;
import com.frj.auth.app.dal.models.UserLoginDataKey;
import com.frj.auth.app.password.PasswordHasher;
import com.frj.auth.app.password.models.PasswordHashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateUserHandlerTest {

    @InjectMocks
    private CreateUserHandler createUserHandler;

    @Mock
    private DataAccessor<UserLoginDataKey, UserLoginData> injectedUserLoginDataAccessor;

    @Mock
    private PasswordHasher injectedPasswordHasher;

    @Captor
    private ArgumentCaptor<UserLoginData> userLoginDataCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUser() throws Exception {
        // == setup ==
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "23456",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );
        when(injectedPasswordHasher.createStorableHash("23456")).thenReturn("hashed-password");
        when(injectedPasswordHasher.matches("23456", "hashed-password")).thenReturn(true);
        doNothing().when(injectedUserLoginDataAccessor).create(any());

        // == execute ==
        final CreateUserReply reply = createUserHandler.createUser(req);

        // == verify ==
        assertNotNull(reply);
        assertNotNull(reply.getFailureMessage());
        verify(injectedUserLoginDataAccessor).create(userLoginDataCaptor.capture());
        assertEquals("user1", userLoginDataCaptor.getValue().getUsername());
        assertEquals("hashed-password", userLoginDataCaptor.getValue().getPassword());
    }
    @Test
    void createUser_HashFailure() throws Exception {
        // == setup ==
        final CreateUserRequest req = new CreateUserRequest(
                "user1",
                "23456",
                CreateUserRequest.UsernameSpec.SIMPLE,
                CreateUserRequest.PasswordSpec.PIN
        );
        when(injectedPasswordHasher.createStorableHash("23456")).thenThrow(new PasswordHashException("fake"));

        // == execute ==
        assertThrows(RuntimeException.class, () -> createUserHandler.createUser(req));

        // == verify ==
        verifyNoMoreInteractions(injectedUserLoginDataAccessor);
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