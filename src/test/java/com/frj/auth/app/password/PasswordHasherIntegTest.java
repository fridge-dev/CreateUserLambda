package com.frj.auth.app.password;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.frj.auth.app.password.algorithms.HashParamsEncoder;
import com.frj.auth.app.password.algorithms.pbkdf2.Pbkdf2PasswordHashingAlgorithm;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Functional integ test for {@link PasswordHasher} module.
 *
 * @author fridge
 */
class PasswordHasherIntegTest {

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

    /**
     * This test shows that verifying a stored hash isn't dependent on current hash generation configuration.
     * This test goes a little bit out of scope for a UT or even a func integ test. It literally duplicates the
     * src logic. But this is helpful in verifying backwards compatibility of future changes.
     */
    @Test
    void hashParamUpdate() throws Exception {
        // Setup
        final String rawPassword = RandomStringUtils.random(10);
        final Pbkdf2PasswordHashingAlgorithm algorithm = new Pbkdf2PasswordHashingAlgorithm();
        final PasswordHashParams params = algorithm.newHashParams();
        final PasswordHashParams oldStoredHashParamsWithoutHash = new PasswordHashParams(
                params.getAlgorithm(),
                params.getIterations() / 2,
                params.getHashLength() / 2,
                randomBytes(params.getSalt().length / 2),
                null
        );

        // Create hash params with "old" configuration
        final byte[] hashedPassword = algorithm.hash(rawPassword, oldStoredHashParamsWithoutHash);
        assertNotEquals(0, hashedPassword.length);
        final PasswordHashParams oldStoredHashParams = oldStoredHashParamsWithoutHash.cloneAndSetHash(hashedPassword);

        // Encode "old" params and then decode
        final HashParamsEncoder hashParamsEncoder = new HashParamsEncoder();
        final String encodedHash = hashParamsEncoder.encodeHash(oldStoredHashParams);
        final PasswordHashParams decodedOldHashParams = hashParamsEncoder.decodeHash(encodedHash);

        // Check that we can still validate the hash, even though their from an "older" configuration.
        final byte[] resurrectedHash = algorithm.hash(rawPassword, decodedOldHashParams);

        assertArrayEquals(resurrectedHash, hashedPassword);
    }

    private byte[] randomBytes(final int length) {
        byte[] bytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(bytes);

        return bytes;
    }
}
