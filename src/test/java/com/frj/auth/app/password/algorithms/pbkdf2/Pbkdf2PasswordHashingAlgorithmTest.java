package com.frj.auth.app.password.algorithms.pbkdf2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.frj.auth.app.password.algorithms.AlgorithmType;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Pbkdf2PasswordHashingAlgorithm} class.
 *
 * This is kind of a functional integ test, since there are no dependencies, and we can't force some of the exception branches.
 *
 * @author fridge
 */
class Pbkdf2PasswordHashingAlgorithmTest {

    private Pbkdf2PasswordHashingAlgorithm algorithm = new Pbkdf2PasswordHashingAlgorithm();

    private String password1;
    private String password2;

    @BeforeEach
    void setup() {
        password1 = RandomStringUtils.random(10);
        password2 = RandomStringUtils.random(30);
    }

    @Test
    void hash_Idempotent() throws Exception {
        PasswordHashParams params = algorithm.newHashParams();

        // Call same method twice
        byte[] hash1 = algorithm.hash(password1, params);
        byte[] hash2 = algorithm.hash(password1, params);

        assertBytesEqual(true, hash1, hash2);
    }

    @Test
    void hash_UniqueResults() throws Exception {
        PasswordHashParams params1 = algorithm.newHashParams();
        PasswordHashParams params2 = algorithm.newHashParams();

        // Ensure that changing password OR changing salt results in different hash.
        byte[] hash1_1 = algorithm.hash(password1, params1);
        byte[] hash1_2 = algorithm.hash(password1, params2);
        byte[] hash2_1 = algorithm.hash(password2, params1);
        byte[] hash2_2 = algorithm.hash(password2, params2);

        assertBytesEqual(false, hash1_1, hash1_2);
        assertBytesEqual(false, hash1_1, hash2_1);
        assertBytesEqual(false, hash1_1, hash2_2);
        assertBytesEqual(false, hash1_2, hash2_1);
        assertBytesEqual(false, hash1_2, hash2_2);
        assertBytesEqual(false, hash2_1, hash2_2);
    }

    @Test
    void newHashParams() throws Exception {
        PasswordHashParams params1 = algorithm.newHashParams();
        PasswordHashParams params2 = algorithm.newHashParams();

        // Assert salts are different
        assertTrue(ArrayUtils.isNotEmpty(params1.getSalt()));
        assertTrue(ArrayUtils.isNotEmpty(params2.getSalt()));
        assertBytesEqual(false, params1.getSalt(), params2.getSalt());

        // Assert algorithm is correct
        assertEquals(AlgorithmType.PBKDF2SHA1, params1.getAlgorithm());
        assertEquals(AlgorithmType.PBKDF2SHA1, params2.getAlgorithm());

        // Iterations should be positive
        assertTrue(params1.getIterations() > 0);
        assertTrue(params2.getIterations() > 0);

        // Hash should be null
        assertNull(params1.getHash());
        assertNull(params2.getHash());
    }

    private void assertBytesEqual(boolean isEqual, byte[] expected, byte[] actual) {
        String failureMessage = String.format(
                "Expected the following two arrays to %s be equal:\n%s\n%s",
                (isEqual ? "" : "NOT"),
                Arrays.toString(expected),
                Arrays.toString(actual)
        );
        assertEquals(isEqual, Arrays.equals(expected, actual), failureMessage);
    }

}