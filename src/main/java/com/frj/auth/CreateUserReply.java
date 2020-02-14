package com.frj.auth;

import java.util.Objects;

/**
 * Top level output.
 *
 * @author fridge
 */
public class CreateUserReply {

    private final String failureMessage;

    public CreateUserReply(final String failureMessage) {
        this.failureMessage = Objects.requireNonNull(failureMessage);
    }
}
