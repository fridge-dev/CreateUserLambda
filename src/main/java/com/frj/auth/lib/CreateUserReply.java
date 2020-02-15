package com.frj.auth.lib;

import java.util.Objects;

/**
 * TODO
 *
 * @author TODO
 */
public class CreateUserReply {

    private final String failureMessage;

    public CreateUserReply(final String failureMessage) {
        this.failureMessage = Objects.requireNonNull(failureMessage);
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}
