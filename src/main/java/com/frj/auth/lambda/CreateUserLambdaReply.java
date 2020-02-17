package com.frj.auth.lambda;

/**
 * Lambda (presentation layer) output for {@link CreateUserLambdaHandler}.
 *
 * @author fridge
 */
public class CreateUserLambdaReply {

    private String failureMessage;

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(final String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
