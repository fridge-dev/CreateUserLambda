package com.frj.auth.app.password.models;

import com.frj.auth.app.password.algorithms.AlgorithmType;

/**
 * Internal model of fields that will be used/encoded into a password hashing result.
 *
 * @author fridge
 */
public class PasswordHashParams {

    private final AlgorithmType algorithm;
    private final int iterations;
    private final int hashLength;
    private final byte[] salt;
    private final byte[] hash;

    public PasswordHashParams(
            final AlgorithmType algorithm,
            final int iterations,
            final int hashLength,
            final byte[] salt,
            final byte[] hash
    ) {
        this.algorithm = algorithm;
        this.iterations = iterations;
        this.hashLength = hashLength;
        this.salt = salt;
        this.hash = hash;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public int getIterations() {
        return iterations;
    }

    public int getHashLength() {
        return hashLength;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getHash() {
        return hash;
    }

    /**
     * My previous impl used lombok's toBuilder(). This is my new variation. (no dependencies!)
     */
    public PasswordHashParams cloneAndSetHash(final byte[] hash) {
        return new PasswordHashParams(
                this.algorithm,
                this.iterations,
                this.hashLength,
                this.salt,
                hash
        );
    }
}