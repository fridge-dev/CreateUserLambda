package com.frj.auth.app.password;

import com.frj.auth.app.password.models.PasswordHashException;
import java.util.Objects;

/**
 * Decorator pattern which performs a sanity check after hashing to ensure we hashed correctly.
 *
 * @author fridge
 */
/* PRIVATE */ class SanityCheckingPasswordHasher implements PasswordHasher {

    private final PasswordHasher delegate;

    SanityCheckingPasswordHasher(final PasswordHasher delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public String createStorableHash(final String password) throws PasswordHashException {
        final String storableHash = delegate.createStorableHash(password);
        sanityCheck(password, storableHash);

        return storableHash;
    }

    private void sanityCheck(final String password, final String storableHash) throws PasswordHashException {
        final boolean matches;
        try {
            matches = delegate.matches(password, storableHash);
        } catch (PasswordHashException e) {
            throw new PasswordHashException("Failed to perform a post-hash sanity validation.", e);
        }

        if (!matches) {
            throw new PasswordHashException("Generated invalid hash.");
        }
    }

    @Override
    public boolean matches(final String rawPassword, final String correctHash) throws PasswordHashException {
        return delegate.matches(rawPassword, correctHash);
    }
}
