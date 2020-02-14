package com.frj.auth;

/**
 * Top level output.
 *
 * @author fridge
 */
public class CreateUserReply {

    private String failureMessage;

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(final String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
