package com.frj.auth.app.password.algorithms;

import com.frj.auth.app.password.models.PasswordHashException;
import com.frj.auth.app.password.models.PasswordHashParams;

/**
 * A PasswordHashingAlgorithm is a specific implementation of a cryptographically secure method of
 * generating hashes of user password that are safe to store in persistent storage.
 *
 * @author fridge
 */
public interface PasswordHashingAlgorithm {

    /**
     * Given the password and hashing configuration params, produce a digest using the underlying crypto algorithm.
     */
    byte[] hash(String password, PasswordHashParams params) throws PasswordHashException;

    /**
     * Create the params for the underlying crypto algorithm to use when producing a hash.
     */
    PasswordHashParams newHashParams();
}