package com.frj.auth.app.password;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Functional integ test for {@link PasswordHasherImpl} class.
 *
 * @author fridge
 */
class PasswordHasherImplIntegTest {

    private PasswordHasher passwordHasher;

    @BeforeEach
    void setup() {
        passwordHasher = PasswordHasher.Factory.create();
    }

    @Test
    void createHashThenVerify() throws Exception {
        String password1 = RandomStringUtils.random(10);
        String password2 = RandomStringUtils.random(15);

        // Create 2 hashes for each password
        String hash1_1 = passwordHasher.createStorableHash(password1);
        String hash1_2 = passwordHasher.createStorableHash(password1);
        String hash2_1 = passwordHasher.createStorableHash(password2);
        String hash2_2 = passwordHasher.createStorableHash(password2);

        // Assert same password produces different hashes on different invocations
        assertNotEquals(hash1_1, hash1_2);
        assertNotEquals(hash2_1, hash2_2);

        // Assert password 1 matches the expected
        assertTrue(passwordHasher.matches(password1, hash1_1));
        assertTrue(passwordHasher.matches(password1, hash1_2));
        assertFalse(passwordHasher.matches(password1, hash2_1));
        assertFalse(passwordHasher.matches(password1, hash2_2));

        // Assert password 2 matches the expected
        assertFalse(passwordHasher.matches(password2, hash1_1));
        assertFalse(passwordHasher.matches(password2, hash1_2));
        assertTrue(passwordHasher.matches(password2, hash2_1));
        assertTrue(passwordHasher.matches(password2, hash2_2));
    }
}
