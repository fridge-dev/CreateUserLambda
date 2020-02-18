package com.frj.auth.app.password;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.frj.auth.app.password.models.PasswordHashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SanityCheckingPasswordHasherTest {

    private static final String RAW_PASSWORD = "snoogles";
    private static final String HASHED_PASSWORD = "OJF(9f8j9*HAF";

    private SanityCheckingPasswordHasher sanityCheckingPasswordHasher;

    @Mock
    private PasswordHasher injectedDelegate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        sanityCheckingPasswordHasher = new SanityCheckingPasswordHasher(injectedDelegate);
    }

    @Test
    void createStorableHash_Success() throws Exception {
        when(injectedDelegate.createStorableHash(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);

        assertEquals(HASHED_PASSWORD, sanityCheckingPasswordHasher.createStorableHash(RAW_PASSWORD));

        verify(injectedDelegate).createStorableHash(RAW_PASSWORD);
        verify(injectedDelegate).matches(RAW_PASSWORD, HASHED_PASSWORD);
    }

    @Test
    void createStorableHash_DelegateException() throws Exception {
        when(injectedDelegate.createStorableHash(RAW_PASSWORD)).thenThrow(new PasswordHashException("fake"));

        assertThrows(PasswordHashException.class, () -> sanityCheckingPasswordHasher.createStorableHash(RAW_PASSWORD));
    }

    @Test
    void createStorableHash_SanityCheckException() throws Exception {
        when(injectedDelegate.createStorableHash(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenThrow(new PasswordHashException("fake"));

        assertThrows(PasswordHashException.class, () -> sanityCheckingPasswordHasher.createStorableHash(RAW_PASSWORD));
    }

    @Test
    void createStorableHash_SanityCheckYieldsFalse() throws Exception {
        when(injectedDelegate.createStorableHash(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertThrows(PasswordHashException.class, () -> sanityCheckingPasswordHasher.createStorableHash(RAW_PASSWORD));
    }

    @Test
    void matches_True() throws Exception {
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);

        assertTrue(sanityCheckingPasswordHasher.matches(RAW_PASSWORD, HASHED_PASSWORD));

        verify(injectedDelegate).matches(RAW_PASSWORD, HASHED_PASSWORD);
    }

    @Test
    void matches_False() throws Exception {
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertFalse(sanityCheckingPasswordHasher.matches(RAW_PASSWORD, HASHED_PASSWORD));

        verify(injectedDelegate).matches(RAW_PASSWORD, HASHED_PASSWORD);
    }

    @Test
    void matches_Exception() throws Exception {
        when(injectedDelegate.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenThrow(new PasswordHashException("fake"));

        assertThrows(PasswordHashException.class, () -> sanityCheckingPasswordHasher.matches(RAW_PASSWORD, HASHED_PASSWORD));
    }
}