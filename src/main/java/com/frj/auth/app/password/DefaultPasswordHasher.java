package com.frj.auth.app.password;

import com.frj.auth.app.password.algorithms.HashParamsEncoder;
import com.frj.auth.app.password.algorithms.PasswordHashingAlgorithm;
import com.frj.auth.app.password.models.PasswordHashException;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.util.Objects;

/**
 * This class is responsible for creating a secure hash of a password that can be stored in a database and used for user authentication.
 * It is not just cryptographically secure hashing, but specializes in hashing of PASSWORDS.
 *
 * This is the top-level entry point of the password package.
 *
 * Research:
 * - https://crackstation.net/hashing-security.htm
 * - https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 * - https://github.com/defuse/password-hashing
 * - https://github.com/jedisct1/libsodium
 *
 * @author fridge
 */
/* PRIVATE */ class DefaultPasswordHasher implements PasswordHasher {

    private final PasswordHashingAlgorithm algorithm;

    private final HashParamsEncoder hashEncoder;

    DefaultPasswordHasher(final PasswordHashingAlgorithm algorithm, final HashParamsEncoder hashEncoder) {
        this.algorithm = Objects.requireNonNull(algorithm);
        this.hashEncoder = Objects.requireNonNull(hashEncoder);
    }

    /**
     * Creates a hashed version of the provided password that is ready/safe to store in a database. The returned hash is already securely
     * encoded with salt and other data needed to verify a login attempt with another password.
     *
     * The returned hash should be used in the method {@link #matches(String, String)} to verify a login.
     */
    public String createStorableHash(final String password) throws PasswordHashException {
        PasswordHashParams paramsWithHash = createHash(password);

        return hashEncoder.encodeHash(paramsWithHash);
    }

    private PasswordHashParams createHash(final String password) throws PasswordHashException {
        PasswordHashParams params = algorithm.newHashParams();

        byte[] hash = algorithm.hash(password, params);

        return params.cloneAndSetHash(hash);
    }

    /**
     * Determine if the provided password matches the provided hash. This can be used to verify a user login attempt (password) against
     * the correct password hash stored in a database. It is assumed that the provided "correct" hash is encoded by what was returned
     * from the {@link #createStorableHash(String)} method.
     */
    public boolean matches(final String rawPassword, final String correctHash) throws PasswordHashException {
        PasswordHashParams params = hashEncoder.decodeHash(correctHash);

        byte[] actualHash = algorithm.hash(rawPassword, params);

        return slowEquals(params.getHash(), actualHash);
    }

    /**
     * This intentionally avoids short-circuit equals, such that the time to execute this method
     * should be the same, regardless of when the first non-equal byte is found.
     */
    private boolean slowEquals(byte[] a, byte[] b) {
        // XOR = 0 if bytes are the same
        int xor = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            xor |= a[i] ^ b[i];
        }
        return xor == 0;
    }

}
