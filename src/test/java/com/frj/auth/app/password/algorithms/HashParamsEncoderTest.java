package com.frj.auth.app.password.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.frj.auth.app.password.models.InvalidHashException;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link HashParamsEncoder} class.
 *
 * @author fridge
 */
class HashParamsEncoderTest {

    private HashParamsEncoder encoder = new HashParamsEncoder();

    @Test
    void encodeHash_DecodeHash() throws Exception {
        for (int i = 0; i < 50; i++) {
            inverseTest(i);
        }
    }

    private void inverseTest(final int iteration) throws Exception {
        PasswordHashParams params = newRandomParams();

        String failureMessage = String.format("Iteration %s failed with params: %s", iteration, params);
        try {
            final PasswordHashParams resurrectedParams = encoder.decodeHash(encoder.encodeHash(params));
            assertTrue(passwordHashParams_equals(params, resurrectedParams), failureMessage);
        } catch (Exception e) {
            throw new RuntimeException(failureMessage, e);
        }
    }

    @Test
    void encodeHash_Idempotent() throws Exception {
        PasswordHashParams params = newRandomParams();

        String digest1 = encoder.encodeHash(params);
        String digest2 = encoder.encodeHash(params);

        assertEquals(digest1, digest2);
    }

    @Test
    void encodeHash_UniqueResults() throws Exception {
        PasswordHashParams params1 = newRandomParams();
        PasswordHashParams params2 = newRandomParams();

        String digest1 = encoder.encodeHash(params1);
        String digest2 = encoder.encodeHash(params2);

        assertNotEquals(digest1, digest2);
    }

    @Test
    void decodeHash_Idempotent() throws Exception {
        String encodedHash = encoder.encodeHash(newRandomParams());
        PasswordHashParams decodedParams1 = encoder.decodeHash(encodedHash);
        PasswordHashParams decodedParams2 = encoder.decodeHash(encodedHash);

        assertTrue(passwordHashParams_equals(decodedParams1, decodedParams2));
    }

    @Test
    void encodeHash_InvalidInput() throws Exception {
        validateIllegalArg_EncodeHash(builder -> builder.algorithm(null));
        validateIllegalArg_EncodeHash(builder -> builder.iterations(0));
        validateIllegalArg_EncodeHash(builder -> builder.iterations(-1));
        validateIllegalArg_EncodeHash(builder -> builder.salt(null));
        validateIllegalArg_EncodeHash(builder -> builder.salt(new byte[0]));
        validateIllegalArg_EncodeHash(builder -> builder.hash(null));
        validateIllegalArg_EncodeHash(builder -> builder.hash(new byte[0]));
        validateIllegalArg_EncodeHash(builder -> builder.hashLength(0));
        validateIllegalArg_EncodeHash(builder -> builder.hashLength(-1));
        validateIllegalArg_EncodeHash(builder -> builder.hash(new byte[5]).hashLength(4));
    }

    private void validateIllegalArg_EncodeHash(final Consumer<PasswordHashParamsBuilder> modifier) throws Exception {
        assertThrows(InvalidHashException.class, () -> encoder.encodeHash(newRandomParams(modifier)));
    }

    @Test
    void decodeHash_InvalidInput() throws Exception {
        // Simple cases
        validateIllegalArg_DecodeHash(null);
        validateIllegalArg_DecodeHash("");
        validateIllegalArg_DecodeHash(" ");

        // Validate that this case doesn't throw exception, since we build all invalid input cases from this example.
        String encoded = encoder.encodeHash(newRandomParams());
        String encodedSalt = encoded.split(":")[3];
        String encodedHashLen = encoded.split(":")[4];
        String encodedHash = encoded.split(":")[5];
        encoder.decodeHash(String.format("1:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Format
        validateIllegalArg_DecodeHash(String.format(":0:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format(":PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s:%s:", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s", encodedSalt, encodedHashLen));

        // Version
        validateIllegalArg_DecodeHash(String.format(":PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("2:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Algorithm
        validateIllegalArg_DecodeHash(String.format("1::123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:MD5Hahaa:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Iterations
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1::%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:0:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Salt
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123::%s:%s", encodedHash, encodedHashLen));

        // Hash length
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s::%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:0:%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:1:%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:1000:%s", encodedSalt, encodedHash));

        // Hash
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:%s:", encodedSalt, encodedHashLen));
    }

    private void validateIllegalArg_DecodeHash(final String encodedHash) throws Exception {
        assertThrows(InvalidHashException.class, () -> encoder.decodeHash(encodedHash));
    }

    private PasswordHashParams newRandomParams(final Consumer<PasswordHashParamsBuilder> modifier) {
        PasswordHashParamsBuilder paramBuilder = newRandomParamsBuilder();

        modifier.accept(paramBuilder);

        return paramBuilder.build();
    }

    private PasswordHashParams newRandomParams() {
        return newRandomParamsBuilder().build();
    }

    private PasswordHashParamsBuilder newRandomParamsBuilder() {
        byte[] hash = randomBytes(50);

        return new PasswordHashParamsBuilder()
                .algorithm(AlgorithmType.PBKDF2SHA1)
                .iterations(randomInt())
                .salt(randomBytes(30))
                .hash(hash)
                .hashLength(hash.length);
    }

    private int randomInt() {
        // Guarantee positive number
        return Math.abs(new Random().nextInt()) + 1;
    }

    private byte[] randomBytes(final int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private static class PasswordHashParamsBuilder {
        private AlgorithmType algorithm;
        private int iterations;
        private int hashLength;
        private byte[] salt;
        private byte[] hash;

        public PasswordHashParamsBuilder algorithm(final AlgorithmType algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public PasswordHashParamsBuilder iterations(final int iterations) {
            this.iterations = iterations;
            return this;
        }

        public PasswordHashParamsBuilder hashLength(final int hashLength) {
            this.hashLength = hashLength;
            return this;
        }

        public PasswordHashParamsBuilder salt(final byte[] salt) {
            this.salt = salt;
            return this;
        }

        public PasswordHashParamsBuilder hash(final byte[] hash) {
            this.hash = hash;
            return this;
        }

        public PasswordHashParams build() {
            return new PasswordHashParams(
                    algorithm,
                    iterations,
                    hashLength,
                    salt,
                    hash
            );
        }
    }

    /**
     * Don't put this in src/, it's not secure.
     */
    private static boolean passwordHashParams_equals(final PasswordHashParams a, final PasswordHashParams b) {
        if (a == b) return true;
        if (b == null || a.getClass() != b.getClass()) return false;
        return a.getIterations() == b.getIterations() &&
                a.getHashLength() == b.getHashLength() &&
                a.getAlgorithm() == b.getAlgorithm() &&
                Arrays.equals(a.getSalt(), b.getSalt()) &&
                Arrays.equals(a.getHash(), b.getHash());
    }
}
