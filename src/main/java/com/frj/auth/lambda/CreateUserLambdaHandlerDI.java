package com.frj.auth.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.auth.app.AppModule;

/**
 * TODO
 *
 * @author TODO
 */
public class CreateUserLambdaHandlerDI implements RequestHandler<CreateUserLambdaRequest, CreateUserLambdaReply> {

    private static final CreateUserLambdaHandler HANDLER = new CreateUserLambdaHandler(
            AppModule.getInstance().getCreateUserHandler()
    );

    @Override
    public CreateUserLambdaReply handleRequest(final CreateUserLambdaRequest input, final Context context) {
        return HANDLER.handleRequest(input, context);
    }
}
