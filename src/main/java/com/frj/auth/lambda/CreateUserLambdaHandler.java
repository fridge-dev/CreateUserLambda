package com.frj.auth.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.frj.auth.app.AppModule;
import com.frj.auth.app.CreateUserHandler;
import com.frj.auth.app.CreateUserReply;
import com.frj.auth.app.CreateUserRequest;

/**
 * My first handler. *tear*
 *
 * @author fridge
 */
public class CreateUserLambdaHandler implements RequestHandler<CreateUserLambdaRequest, CreateUserLambdaReply> {

    private static final CreateUserHandler USER_CREATOR = AppModule.getInstance().getCreateUserHandler();

    public CreateUserLambdaReply handleRequest(final CreateUserLambdaRequest invoke, final Context context) {
        final CreateUserRequest createUserRequest = convertRequest(invoke);
        final CreateUserReply createUserReply = USER_CREATOR.createUser(createUserRequest);
        return convertReply(createUserReply);
    }

    private CreateUserRequest convertRequest(final CreateUserLambdaRequest input) {
        final String[] split = input.getUserSpec().split("/");
        return new CreateUserRequest(
                input.getUsername(),
                input.getPassword(),
                CreateUserRequest.UsernameSpec.valueOf(split[0]),
                CreateUserRequest.PasswordSpec.valueOf(split[1])
        );
    }

    private CreateUserLambdaReply convertReply(final CreateUserReply createUserReply) {
        final CreateUserLambdaReply reply = new CreateUserLambdaReply();
        reply.setFailureMessage(createUserReply.getFailureMessage());
        return reply;
    }
}
