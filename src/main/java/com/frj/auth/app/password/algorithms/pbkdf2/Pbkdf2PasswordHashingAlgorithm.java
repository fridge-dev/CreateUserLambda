package com.frj.auth.app.password.algorithms.pbkdf2;

import com.frj.auth.app.password.algorithms.AlgorithmType;
import com.frj.auth.app.password.algorithms.PasswordHashingAlgorithm;
import com.frj.auth.app.password.models.PasswordHashException;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 (Password-Based Key Derivation Function) using SHA-1 as the hashing function.
 *
 * https://en.wikipedia.org/wiki/PBKDF2
 *
 * Inspired from https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java
 *
 * @author fridge
 */
public class Pbkdf2PasswordHashingAlgorithm implements PasswordHashingAlgorithm {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final SecretKeyFactory PBKDF2_SECRET_KEY_FACTORY = instantiateSecretKeyFactory();
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int BITS_PER_BYTE = 8;

    /**
     * These will be used for generating new password hashes. They should be safe to change without
     * causing issues for existing users.
     */
    private static final int SALT_BYTE_SIZE = 32;
    private static final int PBKDF_ITERATIONS = 1000;
    private static final int HASH_BYTE_SIZE = 32;

    /**
     * @inheritDoc
     */
    @Override
    public byte[] hash(final String password, final PasswordHashParams params) throws PasswordHashException {
        PBEKeySpec keySpec = new PBEKeySpec(
                password.toCharArray(),
                params.getSalt(),
                params.getIterations(),
                params.getHashLength() * BITS_PER_BYTE
        );

        SecretKey secretKey;
        try {
            secretKey = PBKDF2_SECRET_KEY_FACTORY.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new PasswordHashException("Hashing key spec is invalid.", e);
        }

        return secretKey.getEncoded();
    }

    /**
     * @inheritDoc
     *
     * TODO this method can/should be debately decoupled from the #hash method in this class.
     */
    @Override
    public PasswordHashParams newHashParams() {
        byte[] salt = new byte[SALT_BYTE_SIZE];
        RANDOM.nextBytes(salt);

        return new PasswordHashParams(
                AlgorithmType.PBKDF2SHA1,
                PBKDF_ITERATIONS,
                HASH_BYTE_SIZE,
                salt,
                /* hash */ null
        );
    }

    private static SecretKeyFactory instantiateSecretKeyFactory() {
        try {
            return SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not supported.", e);
        }
    }
}