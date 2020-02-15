package com.frj.auth.lambda;

/**
 * Top level output.
 *
 * @author fridge
 */
public class CreateUserInvokeReply {

    private String failureMessage;

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(final String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
