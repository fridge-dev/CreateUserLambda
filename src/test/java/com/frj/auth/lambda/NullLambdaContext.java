package com.frj.auth.lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * A simple impl of Context which returns all nulls. If you need anything more,
 * you should probably just use Mockito.
 *
 * @author fridge
 */
public class NullLambdaContext implements Context {

    @Override
    public String getAwsRequestId() {
        return null;
    }

    @Override
    public String getLogGroupName() {
        return null;
    }

    @Override
    public String getLogStreamName() {
        return null;
    }

    @Override
    public String getFunctionName() {
        return null;
    }

    @Override
    public String getFunctionVersion() {
        return null;
    }

    @Override
    public String getInvokedFunctionArn() {
        return null;
    }

    @Override
    public CognitoIdentity getIdentity() {
        return null;
    }

    @Override
    public ClientContext getClientContext() {
        return null;
    }

    @Override
    public int getRemainingTimeInMillis() {
        return 0;
    }

    @Override
    public int getMemoryLimitInMB() {
        return 0;
    }

    @Override
    public LambdaLogger getLogger() {
        return null;
    }
}
