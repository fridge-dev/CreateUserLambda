package com.frj.auth.app;

import java.util.Objects;

/**
 * The application model response for {@link CreateUserHandler}.
 *
 * @author fridge
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
